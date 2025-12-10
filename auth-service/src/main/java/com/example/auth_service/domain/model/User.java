package com.example.auth_service.domain.model;

import com.example.auth_service.domain.enums.Role;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @jakarta.validation.constraints.Size(max = 100)
    @jakarta.validation.constraints.NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @jakarta.validation.constraints.Size(max = 200)
    @jakarta.validation.constraints.NotNull
    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @Column(name = "role", length = 20)
    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {
    }

    public User(String email, UUID id, String password, Role role) {
        this.email = email;
        this.id = id;
        this.password = password;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}