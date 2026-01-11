package com.example.payment_service.exception;

import java.util.UUID;

public class TransactionInProgressException extends BusinessException {

    public TransactionInProgressException(
            UUID clientId,
            UUID fromAccountId,
            UUID toAccountId
    ) {
        super(
                "Transaction is already in progress for client=" + clientId +
                        ", fromAccount=" + fromAccountId +
                        ", toAccount=" + toAccountId
        );
    }
}