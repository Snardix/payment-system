package com.example.payment_service.service;

import com.example.payment_service.dto.transaction.TransactionCreateRequest;
import com.example.payment_service.model.IdempotentRequest;
import com.example.payment_service.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionFacade {

    private final IdempotencyService idempotencyService;
    private final MoneyTransferService moneyTransferService;
    private final OutboxService outboxService;

    public TransactionFacade(
            IdempotencyService idempotencyService,
            MoneyTransferService moneyTransferService,
            OutboxService outboxService
    ) {
        this.idempotencyService = idempotencyService;
        this.moneyTransferService = moneyTransferService;
        this.outboxService = outboxService;
    }

    public Transaction createTransfer(
            UUID clientId,
            String idempotencyKey,
            TransactionCreateRequest request
    ) {
        // идемпотентность
        IdempotentRequest intent =
                idempotencyService.start(clientId, idempotencyKey, request);

        // деньги — короткая транзакция
        Transaction tx =
                moneyTransferService.transfer(intent);

        // outbox — после коммита
        outboxService.publishTransactionCreated(tx);

        return tx;
    }
}
