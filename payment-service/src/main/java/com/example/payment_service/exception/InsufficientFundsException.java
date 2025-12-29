package com.example.payment_service.exception;

import java.math.BigDecimal;
import java.util.UUID;

public class InsufficientFundsException extends BusinessException {

    public InsufficientFundsException(UUID accountId, BigDecimal balance, BigDecimal amount) {
        super("Insufficient funds on account " + accountId +
                ", balance=" + balance + ", amount=" + amount);
    }
}