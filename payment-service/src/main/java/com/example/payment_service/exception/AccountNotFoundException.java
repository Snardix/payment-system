package com.example.payment_service.exception;

import java.util.UUID;

public class AccountNotFoundException extends BusinessException {

    public AccountNotFoundException(UUID accountId) {
        super("Account not found: " + accountId);
    }
}