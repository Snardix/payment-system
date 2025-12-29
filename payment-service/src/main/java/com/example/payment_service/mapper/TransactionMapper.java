package com.example.payment_service.mapper;

import com.example.payment_service.dto.transaction.TransactionResponse;
import com.example.payment_service.model.Transaction;

public class TransactionMapper {

    private TransactionMapper() {
    }

    public static TransactionResponse toResponse(Transaction transaction) {
        TransactionResponse dto = new TransactionResponse();
        dto.setId(transaction.getId());
        dto.setFromAccountId(transaction.getFromAccount().getClientId());
        dto.setToAccountId(transaction.getToAccount().getClientId());
        dto.setAmount(transaction.getAmount());
        dto.setStatus(transaction.getStatus());
        dto.setCreatedAt(transaction.getCreatedAt());
        return dto;
    }
}