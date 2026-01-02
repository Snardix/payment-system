package com.example.payment_service.controller;

import com.example.payment_service.dto.account.AccountCreateRequest;
import com.example.payment_service.dto.account.AccountResponse;
import com.example.payment_service.dto.account.AccountTopUpRequest;
import com.example.payment_service.jwt.AuthPrincipal;
import com.example.payment_service.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
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
     * Создать аккаунт jwt
     */
    @PostMapping
    public AccountResponse createAccount(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody AccountCreateRequest request
    ) {
        return accountService.createAccount(
                principal.getUserId(),
                request
        );
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
    public List<AccountResponse> getClientAccounts() {
        UUID clientId = (UUID) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return accountService.getClientAccounts(clientId);
    }

    @PatchMapping("/top-up/{id}")
    public AccountResponse topUpAccount(
            @PathVariable UUID id,
            @Valid @RequestBody AccountTopUpRequest request
    ) {
        return accountService.topUpAccount(id, request.getAmount());
    }

    @PatchMapping("/{accountId}/block")
    public void blockAccount(@PathVariable UUID accountId) {
        accountService.blockAccount(accountId);
    }
}
