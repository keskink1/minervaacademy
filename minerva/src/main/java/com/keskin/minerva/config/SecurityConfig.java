package com.keskin.minerva.config;

import com.keskin.minerva.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // No auth
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/status").permitAll()

                        // Authenticated user
                        .requestMatchers(HttpMethod.POST, "/auth/logout").authenticated()
                        .requestMatchers("/auth/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/week-hours").authenticated()
                        .requestMatchers(HttpMethod.POST, "/auth/change-password").authenticated()

                        // Student endpoints (admin)
                        .requestMatchers("/api/students/getAll").hasRole("ADMIN")
                        .requestMatchers("/api/students/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/students/getAllActive").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/students/{studentId}/lessons/{lessonId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/students/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/students/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/students/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/students/{id}/notes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/students/{studentId}/notes/{teacherId}").hasRole("ADMIN")
                        .requestMatchers("/api/students/{id}/notes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/students/{studentId}/lessons/{lessonId}").hasRole("ADMIN")

                        // Teacher endpoints (admin)
                        .requestMatchers("/api/teachers/getAll").hasRole("ADMIN")
                        .requestMatchers("/api/teachers/getAllActive").hasRole("ADMIN")
                        .requestMatchers("/api/teachers/{teacherId}/lessons").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/teachers/{teacherId}/lessons/{lessonId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/teachers/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/teachers/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/teachers/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/teachers/{teacherId}/lessons/{lessonId}").hasRole("ADMIN")

                        // Lesson endpoints (admin)
                        .requestMatchers("/api/lessons/getAll").hasRole("ADMIN")
                        .requestMatchers("/api/lessons/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/lessons/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/lessons/{id}").hasRole("ADMIN")

                        // block other endpoints
                        .anyRequest().denyAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // 401
                        .accessDeniedHandler((req, res, e) -> res.setStatus(HttpStatus.FORBIDDEN.value())) // 403
                );

        return http.build();
    }

}

