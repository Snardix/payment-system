package com.example.auth_service.service;

import com.example.auth_service.api.dto.AuthRequest;
import com.example.auth_service.api.dto.AuthResponse;
import com.example.auth_service.api.dto.RegisterRequest;
import com.example.auth_service.api.exception.AuthException;
import com.example.auth_service.api.mapper.UserMapper;
import com.example.auth_service.domain.model.User;
import com.example.auth_service.domain.enums.Role;
import com.example.auth_service.domain.repository.UserRepository;
import com.example.auth_service.security.jwt.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository repository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {

        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new AuthException("Пользователь уже существует");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        repository.save(user);

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthResponse(token, UserMapper.toDto(user));
    }

    public AuthResponse login(AuthRequest request) {

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("Неверный логин"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException("Неверный пароль");
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthResponse(token, UserMapper.toDto(user));
    }

    public User getUserByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new AuthException("Пользователь не найден"));
    }
}
