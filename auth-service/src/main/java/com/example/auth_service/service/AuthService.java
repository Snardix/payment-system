package com.example.auth_service.service;

import com.example.auth_service.api.dto.AuthRequest;
import com.example.auth_service.api.dto.AuthResponse;
import com.example.auth_service.api.dto.RegisterRequest;
import com.example.auth_service.api.exception.AuthException;
import com.example.auth_service.api.mapper.UserMapper;
import com.example.auth_service.domain.model.User;
import com.example.auth_service.domain.enums.Role;
import com.example.auth_service.domain.repository.UserRepository;
import com.example.auth_service.kafka.producer.AuthEventProducer;
import com.example.auth_service.security.jwt.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthEventProducer authEventProducer;

    public AuthService(UserRepository repository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService, AuthEventProducer authEventProducer) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authEventProducer = authEventProducer;
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

        authEventProducer.sendRegistrationEvent(user.getEmail());

        String token = jwtService.generateToken(
                user.getId().toString(),
                user.getRole().name(),
                user.getEmail()
        );

        return new AuthResponse(token, UserMapper.toDto(user));
    }

    public AuthResponse login(AuthRequest request) {

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("Неверный логин"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException("Неверный пароль");
        }

        authEventProducer.sendAuthEvent(user.getEmail());

        String token = jwtService.generateToken(
                user.getId().toString(),
                user.getRole().name(),
                user.getEmail()
        );

        return new AuthResponse(token, UserMapper.toDto(user));
    }

    public User getUserByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new AuthException("Пользователь не найден"));
    }
}
