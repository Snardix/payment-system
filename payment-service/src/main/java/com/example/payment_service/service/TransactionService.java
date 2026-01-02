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

        UUID fromAccountId = request.getFromAccountId();
        UUID toAccountId = request.getToAccountId();
        BigDecimal amount = request.getAmount();

        // 1. Проверка: разные счета
        if (fromAccountId.equals(toAccountId)) {
            throw new SameAccountTransferException();
        }

        // 2. Аккаунт отправителя
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException(fromAccountId));

        // 3. Проверка владельца
        if (!fromAccount.getClientId().equals(clientId)) {
            throw new AccountOwnershipException(fromAccountId);
        }

        // 4. Аккаунт получателя
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException(toAccountId));

        // 5. Валидация перевода
        validateTransfer(fromAccount, toAccount, amount);

        // 6. Балансы
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        // 7. Транзакция
        Transaction transaction = new Transaction(fromAccount, toAccount, amount);
        transaction.setStatus(TransactionStatus.SUCCESS);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction saved = transactionRepository.save(transaction);

        OutboxEvent event = new OutboxEvent(
                "TRANSACTION",
                saved.getId(),
                "TRANSACTION_CREATED",
                buildPaymentEvent(saved)
        );

        outboxRepository.save(event);

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