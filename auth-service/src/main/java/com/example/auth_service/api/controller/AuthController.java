package com.example.auth_service.api.controller;

import com.example.auth_service.api.dto.AuthRequest;
import com.example.auth_service.api.dto.AuthResponse;
import com.example.auth_service.api.dto.RegisterRequest;
import com.example.auth_service.api.mapper.UserMapper;
import com.example.auth_service.domain.model.User;
import com.example.auth_service.security.jwt.JwtService;
import com.example.auth_service.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;
    private final JwtService jwtService;

    public AuthController(AuthService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return service.login(request);
    }

    @GetMapping("/me")
    public AuthResponse me(@RequestHeader("Authorization") String authHeader) {
        // Извлекаем токен (Bearer xxx)
        String token = authHeader.replace("Bearer ", "");

        // Получаем email и роль из токена
        String email = jwtService.getUsername(token);
        String role = jwtService.getRole(token);

        // Ищем юзера в БД
        User user = service.getUserByEmail(email);

        return new AuthResponse(token, UserMapper.toDto(user));
    }
}
