package com.example.payment_service.service;

import com.example.payment_service.dto.transaction.TransactionCreateRequest;
import com.example.payment_service.exception.IdempotencyKeyAlreadyUsedException;
import com.example.payment_service.model.IdempotentRequest;
import com.example.payment_service.repository.IdempotencyRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class IdempotencyService {

    private final IdempotencyRepository repository;

    public IdempotencyService(IdempotencyRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public IdempotentRequest start(
            UUID clientId,
            String key,
            TransactionCreateRequest request
    ) {
        try {
            return repository.save(
                    new IdempotentRequest(
                            clientId,
                            key,
                            request.getFromAccountId(),
                            request.getToAccountId(),
                            request.getAmount()
                    )
            );
        } catch (DataIntegrityViolationException e) {
            throw new IdempotencyKeyAlreadyUsedException(key);
        }
    }
}
