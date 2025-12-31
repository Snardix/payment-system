package com.example.auth_service.api.controller;

import com.example.auth_service.api.dto.AuthRequest;
import com.example.auth_service.api.dto.AuthResponse;
import com.example.auth_service.api.dto.RegisterRequest;
import com.example.auth_service.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return service.login(request);
    }
}
