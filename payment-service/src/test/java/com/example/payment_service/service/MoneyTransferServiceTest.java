package com.example.payment_service.service;

import com.example.payment_service.repository.AccountRepository;
import com.example.payment_service.repository.IdempotencyRepository;
import com.example.payment_service.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MoneyTransferServiceTest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    IdempotencyRepository idempotencyRepository;

    @InjectMocks
    MoneyTransferService moneyTransferService;

    @Test
    void transfer() {
        
    }
}