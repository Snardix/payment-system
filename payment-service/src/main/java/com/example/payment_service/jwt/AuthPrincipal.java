package com.example.payment_service.jwt;

import java.util.UUID;

public class AuthPrincipal {

    private final UUID userId;
    private final String email;

    public AuthPrincipal(UUID userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
}