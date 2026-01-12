package com.example.payment_service.repository;

import com.example.payment_service.model.IdempotentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface IdempotencyRepository extends JpaRepository<IdempotentRequest, UUID> {
    @Modifying
    @Query("""
UPDATE IdempotentRequest r
SET r.transactionId = :txId, r.status = 'SUCCESS'
WHERE r.id = :id
""")
    void markSuccess(UUID id, UUID txId);
}