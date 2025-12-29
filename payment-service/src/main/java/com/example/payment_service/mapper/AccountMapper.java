package com.example.payment_service.mapper;

import com.example.payment_service.dto.account.AccountResponse;
import com.example.payment_service.model.Account;

public class AccountMapper {

    private AccountMapper() {
    }

    public static AccountResponse toResponse(Account account) {
        AccountResponse dto = new AccountResponse();
        dto.setId(account.getId());
        dto.setBalance(account.getBalance());
        dto.setCurrency(account.getCurrency());
        dto.setStatus(account.getStatus());
        dto.setCreatedAt(account.getCreatedAt());
        return dto;
    }
}