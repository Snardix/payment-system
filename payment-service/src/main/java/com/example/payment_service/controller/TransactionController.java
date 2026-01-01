package com.example.payment_service.controller;

import com.example.payment_service.dto.transaction.TransactionCreateRequest;
import com.example.payment_service.dto.transaction.TransactionResponse;
import com.example.payment_service.mapper.TransactionMapper;
import com.example.payment_service.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public TransactionResponse create(
            Authentication authentication,
            @Valid @RequestBody TransactionCreateRequest request
    ) {
        UUID clientId = UUID.fromString(authentication.getName());

        return TransactionMapper.toResponse(
                transactionService.createTransfer(clientId, request)
        );
    }

    @GetMapping("/outgoing")
    public List<TransactionResponse> outgoing(Authentication authentication) {
        UUID clientId = UUID.fromString(authentication.getName());

        return transactionService.getOutgoing(clientId)
                .stream()
                .map(TransactionMapper::toResponse)
                .toList();
    }

    @GetMapping("/incoming")
    public List<TransactionResponse> incoming(Authentication authentication) {
        UUID clientId = UUID.fromString(authentication.getName());

        return transactionService.getIncoming(clientId)
                .stream()
                .map(TransactionMapper::toResponse)
                .toList();
    }
}