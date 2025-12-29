package com.example.payment_service.exception;

public class SameAccountTransferException extends BusinessException {

    public SameAccountTransferException() {
        super("Transfer between the same accounts is not allowed");
    }
}