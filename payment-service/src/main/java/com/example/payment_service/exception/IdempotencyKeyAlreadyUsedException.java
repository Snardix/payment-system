package com.example.payment_service.exception;

public class IdempotencyKeyAlreadyUsedException extends BusinessException {
    public IdempotencyKeyAlreadyUsedException(String idempotencyKey) {
        super(idempotencyKey + " this key has been procassed");
    }
}
