package com.example.payment_service.controller;

import com.example.payment_service.dto.account.AccountCreateRequest;
import com.example.payment_service.dto.account.AccountResponse;
import com.example.payment_service.dto.account.AccountTopUpRequest;
import com.example.payment_service.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Создать аккаунт
     * clientId пока передаём явно (позже будет из JWT)
     */
    @PostMapping
    public AccountResponse createAccount(
            @RequestParam UUID clientId,
            @Valid @RequestBody AccountCreateRequest request
    ) {
        return accountService.createAccount(clientId, request);
    }

    /**
     * Получить аккаунт по id
     */
    @GetMapping("/{id}")
    public AccountResponse getAccount(@PathVariable UUID id) {
        return accountService.getAccount(id);
    }

    /**
     * Получить все аккаунты клиента
     */
    @GetMapping
    public List<AccountResponse> getClientAccounts(@RequestParam UUID clientId) {
        return accountService.getClientAccounts(clientId);
    }

    @PatchMapping("/{id}/top-up")
    public AccountResponse topUpAccount(
            @PathVariable UUID id,
            @Valid @RequestBody AccountTopUpRequest request
    ) {
        return accountService.topUpAccount(id, request.getAmount());
    }
}
