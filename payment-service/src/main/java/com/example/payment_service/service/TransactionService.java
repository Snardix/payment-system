package com.example.payment_service.service;

import com.example.payment_service.dto.event.PaymentEventDto;
import com.example.payment_service.dto.transaction.TransactionCreateRequest;
import com.example.payment_service.enums.AccountStatus;
import com.example.payment_service.enums.TransactionStatus;
import com.example.payment_service.exception.*;
import com.example.payment_service.jwt.AuthPrincipal;
import com.example.payment_service.model.Account;
import com.example.payment_service.model.IdempotentRequest;
import com.example.payment_service.model.OutboxEvent;
import com.example.payment_service.model.Transaction;
import com.example.payment_service.repository.AccountRepository;
import com.example.payment_service.repository.IdempotencyRepository;
import com.example.payment_service.repository.OutboxRepository;
import com.example.payment_service.repository.TransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getOutgoing(UUID clientId) {
        return transactionRepository.findOutgoing(clientId);
    }

    public List<Transaction> getIncoming(UUID clientId) {
        return transactionRepository.findIncoming(clientId);
    }
}