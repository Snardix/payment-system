package com.example.payment_service.repository;

import com.example.payment_service.model.Transaction;
import com.example.payment_service.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByFromAccount_ClientId(UUID clientId);

    List<Transaction> findByToAccount_ClientId(UUID clientId);

}
