package com.example.payment_service.service;

import com.example.payment_service.dto.event.PaymentEventDto;
import com.example.payment_service.dto.transaction.TransactionCreateRequest;
import com.example.payment_service.enums.AccountStatus;
import com.example.payment_service.enums.TransactionStatus;
import com.example.payment_service.exception.*;
import com.example.payment_service.jwt.AuthPrincipal;
import com.example.payment_service.model.Account;
import com.example.payment_service.model.OutboxEvent;
import com.example.payment_service.model.Transaction;
import com.example.payment_service.repository.AccountRepository;
import com.example.payment_service.repository.OutboxRepository;
import com.example.payment_service.repository.TransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              OutboxRepository outboxRepository,
                              ObjectMapper objectMapper) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Transaction createTransfer(UUID clientId, TransactionCreateRequest request) {

        UUID fromId = request.getFromAccountId();
        UUID toId = request.getToAccountId();
        BigDecimal amount = request.getAmount();

        if (fromId.equals(toId)) {
            throw new SameAccountTransferException();
        }

        Account from = accountRepository.findById(fromId)
                .orElseThrow(() -> new AccountNotFoundException(fromId));

        if (!from.getClientId().equals(clientId)) {
            throw new AccountOwnershipException(fromId);
        }

        Account to = accountRepository.findById(toId)
                .orElseThrow(() -> new AccountNotFoundException(toId));

        int withdrawn = accountRepository.withdraw(fromId, amount);
        if (withdrawn == 0) {
            throw new InsufficientFundsException(fromId, from.getBalance(), amount);
        }

        accountRepository.deposit(toId, amount);

        Transaction tx = new Transaction(from, to, amount);
        tx.setStatus(TransactionStatus.SUCCESS);

        Transaction saved = transactionRepository.save(tx);

        outboxRepository.save(
                new OutboxEvent(
                        "TRANSACTION",
                        saved.getId(),
                        "TRANSACTION_CREATED",
                        buildPaymentEvent(saved)
                )
        );

        return saved;
    }


    private String buildPaymentEvent(Transaction tx) {

        PaymentEventDto.Payload payload = new PaymentEventDto.Payload();
        payload.setTransactionId(tx.getId().toString());
        payload.setStatus(tx.getStatus().name());
        payload.setAmount(tx.getAmount().toString());

        payload.setEmail(getCurrentUserEmail());

        PaymentEventDto event = new PaymentEventDto();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventType("TRANSACTION_CREATED");
        event.setAggregateId(tx.getId().toString());
        event.setOccurredAt(Instant.now().toString());
        event.setPayload(payload);

        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCurrentUserEmail() {
        Object principal =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        return ((AuthPrincipal) principal).getEmail();
    }

    public List<Transaction> getOutgoing(UUID clientId) {
        return transactionRepository.findByFromAccount_ClientId(clientId);
    }

    public List<Transaction> getIncoming(UUID clientId) {
        return transactionRepository.findByToAccount_ClientId(clientId);
    }

    private void validateTransfer(Account from, Account to, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException(amount);
        }

        if (from.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountBlockedException(from.getId());
        }

        if (to.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountBlockedException(to.getId());
        }

        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(
                    from.getId(),
                    from.getBalance(),
                    amount
            );
        }
    }
}