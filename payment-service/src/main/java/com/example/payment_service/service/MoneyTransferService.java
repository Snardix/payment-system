package com.example.payment_service.service;


import com.example.payment_service.enums.TransactionStatus;
import com.example.payment_service.exception.InsufficientFundsException;
import com.example.payment_service.model.IdempotentRequest;
import com.example.payment_service.model.Transaction;
import com.example.payment_service.repository.AccountRepository;
import com.example.payment_service.repository.IdempotencyRepository;
import com.example.payment_service.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class MoneyTransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final IdempotencyRepository idempotencyRepository;

    public MoneyTransferService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            IdempotencyRepository idempotencyRepository
    ) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.idempotencyRepository = idempotencyRepository;
    }

    @Transactional
    public Transaction transfer(IdempotentRequest intent) {

        int withdrawn = accountRepository.withdraw(
                intent.getFromAccountId(),
                intent.getAmount()
        );

        if (withdrawn == 0) {
            throw new InsufficientFundsException(
                    intent.getFromAccountId(),
                    BigDecimal.ZERO,
                    intent.getAmount()
            );
        }

        accountRepository.deposit(
                intent.getToAccountId(),
                intent.getAmount()
        );

        Transaction tx = new Transaction(
                intent.getFromAccountId(),
                intent.getToAccountId(),
                intent.getAmount()
        );
        tx.setStatus(TransactionStatus.SUCCESS);

        Transaction saved = transactionRepository.save(tx);

        idempotencyRepository.markSuccess(intent.getId(), saved.getId());

        return saved;
    }
}
