package com.keskin.minerva.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
public final class StatusController {

    private static final Logger logger = LoggerFactory.getLogger(StatusController.class);

    private StatusController() {
    }

    @Operation(
            summary = "Check application status",
            description = "Returns a simple message indicating that the application is running.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Application is up and running")
            }
    )
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getPrincipal());
        logger.info("Received request to check application status.");
        return ResponseEntity.ok("Minervacademy is up and running!");
    }
}
