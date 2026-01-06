package com.example.payment_service.repository;

import com.example.payment_service.enums.AccountStatus;
import com.example.payment_service.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    List<Account> findAllByClientId(UUID clientId);

    Optional<Account> findByIdAndStatus(UUID id, AccountStatus status);

    Optional<Account> findByClientId(UUID id);

    @Modifying
    @Query("""
        UPDATE Account a
        SET a.balance = a.balance - :amount
        WHERE a.id = :id
          AND a.balance >= :amount
          AND a.status = 'ACTIVE'
    """)
    int withdraw(UUID id, BigDecimal amount);

    @Modifying
    @Query("""
        UPDATE Account a
        SET a.balance = a.balance + :amount
        WHERE a.id = :id
          AND a.status = 'ACTIVE'
    """)
    int deposit(UUID id, BigDecimal amount);
}
