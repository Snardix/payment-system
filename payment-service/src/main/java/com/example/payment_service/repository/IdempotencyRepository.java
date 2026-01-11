package com.example.payment_service.repository;

import com.example.payment_service.model.IdempotentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface IdempotencyRepository extends JpaRepository<IdempotentRequest, UUID> {

    Optional<IdempotentRequest> findByClientIdAndFromAccountIdAndToAccountIdAndAmount(
            UUID clientId,
            UUID fromAccountId,
            UUID toAccountId,
            BigDecimal amount
    );
}