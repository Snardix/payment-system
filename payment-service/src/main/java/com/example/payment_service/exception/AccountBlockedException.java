package com.example.payment_service.exception;

import java.util.UUID;

public class AccountBlockedException extends BusinessException {

    public AccountBlockedException(UUID accountId) {
        super("Account is blocked: " + accountId);
    }
}