package com.example.payment_service.exception;

import java.util.UUID;

public class TransactionNotFoundException extends BusinessException {

    public TransactionNotFoundException(UUID transactionId) {
        super("Transaction not found: " + transactionId);
    }
}