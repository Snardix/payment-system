package com.example.payment_service.service;

import com.example.payment_service.dto.event.PaymentEventDto;
import com.example.payment_service.jwt.AuthPrincipal;
import com.example.payment_service.model.OutboxEvent;
import com.example.payment_service.model.Transaction;
import com.example.payment_service.repository.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class OutboxService {

    private final OutboxRepository repository;
    private final ObjectMapper mapper;

    public OutboxService(
            OutboxRepository repository,
            ObjectMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public void publishTransactionCreated(Transaction tx) {
        repository.save(
                new OutboxEvent(
                        "TRANSACTION",
                        tx.getId(),
                        "TRANSACTION_CREATED",
                        buildEvent(tx)
                )
        );
    }

    private String buildEvent(Transaction tx) {
        try {
            PaymentEventDto dto = new PaymentEventDto();
            dto.setEventId(UUID.randomUUID().toString());
            dto.setEventType("TRANSACTION_CREATED");
            dto.setAggregateId(tx.getId().toString());
            dto.setOccurredAt(Instant.now().toString());

            PaymentEventDto.Payload p = new PaymentEventDto.Payload();
            String email = getCurrentUserEmail();
            p.setEmail(email);
            p.setTransactionId(tx.getId().toString());
            p.setAmount(tx.getAmount().toString());
            p.setStatus(tx.getStatus().name());

            dto.setPayload(p);

            return mapper.writeValueAsString(dto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AuthPrincipal principal) {
            return principal.getEmail();
        }
        return null;
    }
}
