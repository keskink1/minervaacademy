package com.keskin.minerva.controllers;

import com.keskin.minerva.config.JwtConfig;
import com.keskin.minerva.dtos.LoginRequest;
import com.keskin.minerva.dtos.responses.JwtResponse;
import com.keskin.minerva.dtos.users.ChangePasswordRequestDto;
import com.keskin.minerva.jwt.IJwtService;
import com.keskin.minerva.services.IUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
@Validated
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final IJwtService jwtService;
    private final IUserService userService;


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginRequest(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );

        var user = userService.findByEmail(request.getEmail()).orElseThrow();

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(
            @CookieValue(value = "refreshToken") String refreshToken
    ){
        var jwt = jwtService.parseToken(refreshToken);
        if(jwt == null || jwt.isExpired()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user = userService.getUserById(jwt.getUserId()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {

        // delete refresh token
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        System.out.println("User logged out");
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            Principal principal,
           @Valid @RequestBody ChangePasswordRequestDto request) {
        userService.changePassword(principal.getName(), request);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/me")
    public ResponseEntity<?> me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long)authentication.getPrincipal();

        var meDto = userService.getMeDtoById(userId);

        if (meDto == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(meDto);
    }
}