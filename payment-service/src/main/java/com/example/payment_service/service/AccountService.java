package com.example.payment_service.service;

import com.example.payment_service.dto.account.AccountCreateRequest;
import com.example.payment_service.dto.account.AccountResponse;
import com.example.payment_service.enums.AccountStatus;
import com.example.payment_service.exception.AccountBlockedException;
import com.example.payment_service.exception.AccountNotFoundException;
import com.example.payment_service.mapper.AccountMapper;
import com.example.payment_service.model.Account;
import com.example.payment_service.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Создание нового аккаунта
     */
    @Transactional
    public AccountResponse createAccount(UUID clientId, AccountCreateRequest request) {
        Account account = new Account(clientId, request.getCurrency());
        Account saved = accountRepository.save(account);
        return AccountMapper.toResponse(saved);
    }

    /**
     * Получить аккаунт по id
     */
    @Transactional(readOnly = true)
    public AccountResponse getAccount(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException(accountId)
                );

        if (account.getStatus() == AccountStatus.BLOCKED) {
            throw new AccountBlockedException(accountId);
        }

        return AccountMapper.toResponse(account);
    }

    /**
     * Получить все аккаунты клиента
     */
    @Transactional(readOnly = true)
    public List<AccountResponse> getClientAccounts(UUID clientId) {
        return accountRepository.findAllByClientId(clientId)
                .stream()
                .map(AccountMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Заблокировать аккаунт
     */
    @Transactional
    public void blockAccount(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException(accountId)
                );

        account.setStatus(AccountStatus.BLOCKED);
    }

    @Transactional
    public AccountResponse topUpAccount(UUID accountId, BigDecimal amount) {

        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Top-up amount must be positive");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        if (account.getStatus() == AccountStatus.BLOCKED) {
            throw new AccountBlockedException(accountId);
        }

        account.setBalance(account.getBalance().add(amount));

        return AccountMapper.toResponse(account);
    }
}
