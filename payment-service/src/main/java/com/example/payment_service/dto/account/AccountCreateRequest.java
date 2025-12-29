package com.example.payment_service.dto.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AccountCreateRequest {

    @NotNull
    @Size(min = 3, max = 3)
    private String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}