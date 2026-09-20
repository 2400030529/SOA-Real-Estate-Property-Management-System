
package com.klu.springmvc.controller;

import com.klu.springmvc.dto.LoginRequest;
import com.klu.springmvc.dto.LoginResponse;
import com.klu.springmvc.dto.RegisterRequest;
import com.klu.springmvc.service.AuthService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request) {

        String response = authService.register(request);

        if (response.equals("Email already registered")) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> profile(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                "Authenticated user: " + email
        );
    }
}