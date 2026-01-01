package com.example.payment_service.exception;

import java.math.BigDecimal;

public class InvalidAmountException extends BusinessException {

    public InvalidAmountException(BigDecimal amount) {
        super("Invalid transfer amount: " + amount);
    }
}