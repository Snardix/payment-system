package com.example.payment_service.outbox;

import com.example.payment_service.repository.OutboxRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@EnableScheduling
public class OutboxCleanupJob {

    private final OutboxRepository repository;

    public OutboxCleanupJob(OutboxRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void cleanup() {

        Instant now = Instant.now();

        repository.deleteAll(
                repository.findAll().stream()
                        .filter(e ->
                                (e.getStatus().equals("SENT")
                                        && e.getSentAt().isBefore(now.minusSeconds(60)))
                                        ||
                                        (e.getStatus().equals("DEAD")
                                                && e.getCreatedAt().isBefore(now.minusSeconds(30)))
                        )
                        .toList()
        );
    }
}