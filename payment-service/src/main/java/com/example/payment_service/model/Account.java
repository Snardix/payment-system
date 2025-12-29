package com.example.payment_service.model;

import com.example.payment_service.enums.AccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @NotNull
    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @NotNull
    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @NotNull
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @NotNull
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    public Account() {
    }

    public Account(UUID clientId, String currency) {
        this.clientId = clientId;
        this.balance = BigDecimal.ZERO;
        this.currency = currency;
        this.status = AccountStatus.ACTIVE;
    }

    public UUID getId() {
        return id;
    }

    public @NotNull UUID getClientId() {
        return clientId;
    }

    public @NotNull BigDecimal getBalance() {
        return balance;
    }

    public @NotNull String getCurrency() {
        return currency;
    }

    public @NotNull AccountStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setClientId(@NotNull UUID clientId) {
        this.clientId = clientId;
    }

    public void setBalance(@NotNull BigDecimal balance) {
        this.balance = balance;
    }

    public void setCurrency(@NotNull String currency) {
        this.currency = currency;
    }

    public void setStatus(@NotNull AccountStatus status) {
        this.status = status;
    }
}