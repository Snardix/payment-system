package com.example.payment_service.exception;

public class InvalidAccountException extends BusinessException {

    public InvalidAccountException() {
        super("Invalid account id");
    }
}