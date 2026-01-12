package com.example.payment_service.repository;

import com.example.payment_service.model.Transaction;
import com.example.payment_service.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("""
        SELECT t
        FROM Transaction t
        JOIN Account a ON a.id = t.fromAccountId
        WHERE a.clientId = :clientId
    """)
    List<Transaction> findOutgoing(UUID clientId);

    @Query("""
        SELECT t
        FROM Transaction t
        JOIN Account a ON a.id = t.toAccountId
        WHERE a.clientId = :clientId
    """)
    List<Transaction> findIncoming(UUID clientId);

}
