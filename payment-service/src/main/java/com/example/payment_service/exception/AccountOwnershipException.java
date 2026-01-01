package com.example.payment_service.exception;

import java.util.UUID;

public class AccountOwnershipException extends BusinessException {

    public AccountOwnershipException(UUID accountId) {
        super("Account does not belong to current client: " + accountId);
    }
}