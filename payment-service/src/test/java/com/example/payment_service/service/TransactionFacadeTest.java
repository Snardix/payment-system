package com.example.payment_service.service;

import com.example.payment_service.dto.transaction.TransactionCreateRequest;
import com.example.payment_service.model.IdempotentRequest;
import com.example.payment_service.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TransactionFacadeTest {

    @Mock
    IdempotencyService idempotencyService;

    @Mock
    MoneyTransferService moneyTransferService;

    @Mock
    OutboxService outboxService;

    @InjectMocks
    TransactionFacade transactionFacade;

    @Test
    void createTransfer() {
        UUID clientId = UUID.randomUUID();
        String key = "idem-key";
        TransactionCreateRequest request = new TransactionCreateRequest();

        IdempotentRequest intent = new IdempotentRequest();
        Transaction tx = new Transaction();

        when(idempotencyService.start(clientId, key, request)).thenReturn(intent);
        when(moneyTransferService.transfer(intent)).thenReturn(tx);

        Transaction result = transactionFacade.createTransfer(clientId, key, request);

        assertEquals(tx, result);

        verify(idempotencyService)
                .start(clientId, key, request);

        verify(moneyTransferService)
                .transfer(intent);

        verify(outboxService)
                .publishTransactionCreated(tx);
    }
}