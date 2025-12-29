package com.example.payment_service.mapper;

import com.example.payment_service.dto.event.PaymentEventDto;
import com.example.payment_service.enums.PaymentEventType;
import com.example.payment_service.model.Account;
import com.example.payment_service.model.Transaction;

import java.time.Instant;

public class PaymentEventMapper {

    private PaymentEventMapper() {
    }

    public static PaymentEventDto accountEvent(
            PaymentEventType type,
            Account account
    ) {
        PaymentEventDto event = new PaymentEventDto();
        event.setEventType(type);
        event.setClientId(account.getClientId());
        event.setAccountId(account.getId());
        event.setTimestamp(Instant.now());
        return event;
    }

    public static PaymentEventDto transactionEvent(
            PaymentEventType type,
            Transaction transaction
    ) {
        PaymentEventDto event = new PaymentEventDto();
        event.setEventType(type);
        event.setClientId(transaction.getFromAccount().getClientId());
        event.setAccountId(transaction.getFromAccount().getId());
        event.setTransactionId(transaction.getId());
        event.setAmount(transaction.getAmount());
        event.setTimestamp(Instant.now());
        return event;
    }
}