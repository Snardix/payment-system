package com.example.payment_service.model;

import com.example.payment_service.enums.TransactionStatus;
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
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "from_account_id", nullable = false)
    private UUID fromAccountId;

    @Column(name = "to_account_id", nullable = false)
    private UUID toAccountId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @CreationTimestamp
    private Instant createdAt;

    public Transaction() {}

    public Transaction(UUID fromAccountId, UUID toAccountId, BigDecimal amount) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.status = TransactionStatus.CREATED;
    }

    public UUID getId() {
        return id;
    }

    public UUID getFromAccount() {
        return fromAccountId;
    }

    public UUID getToAccount() {
        return toAccountId;
    }

    public @NotNull BigDecimal getAmount() {
        return amount;
    }

    public @NotNull TransactionStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UUID getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(UUID fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public UUID getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(UUID toAccountId) {
        this.toAccountId = toAccountId;
    }

    public void setAmount(@NotNull BigDecimal amount) {
        this.amount = amount;
    }

    public void setStatus( @NotNull TransactionStatus status) {
        this.status = status;
    }
}