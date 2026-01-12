package com.example.payment_service.controller;

import com.example.payment_service.dto.transaction.TransactionCreateRequest;
import com.example.payment_service.dto.transaction.TransactionResponse;
import com.example.payment_service.jwt.AuthPrincipal;
import com.example.payment_service.mapper.TransactionMapper;
import com.example.payment_service.service.TransactionFacade;
import com.example.payment_service.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionFacade transactionFacade;

    public TransactionController(TransactionService transactionService, TransactionFacade transactionFacade) {
        this.transactionService = transactionService;
        this.transactionFacade = transactionFacade;
    }

    @PostMapping
    public TransactionResponse create(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody TransactionCreateRequest request
    ) {
        return TransactionMapper.toResponse(
                transactionFacade.createTransfer(
                        principal.getUserId(),
                        idempotencyKey,
                        request
                )
        );
    }

    @GetMapping("/outgoing")
    public List<TransactionResponse> outgoing(
            @AuthenticationPrincipal AuthPrincipal principal
    ) {
        return transactionService.getOutgoing(principal.getUserId())
                .stream()
                .map(TransactionMapper::toResponse)
                .toList();
    }

    @GetMapping("/incoming")
    public List<TransactionResponse> incoming(
            @AuthenticationPrincipal AuthPrincipal principal
    ) {
        return transactionService.getIncoming(principal.getUserId())
                .stream()
                .map(TransactionMapper::toResponse)
                .toList();
    }
}
