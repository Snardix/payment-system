package com.example.payment_service.outbox;

import com.example.payment_service.enums.OutboxStatus;
import com.example.payment_service.kafka.producer.PaymentEventProducer;
import com.example.payment_service.model.OutboxEvent;
import com.example.payment_service.repository.OutboxRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.time.Instant;
import java.util.List;

import static org.apache.kafka.common.requests.DeleteAclsResponse.log;

@Component
@EnableScheduling
public class OutboxWorker {

    private static final int BATCH_SIZE = 10;
    private static final int MAX_RETRIES = 2;

    private final OutboxRepository outboxRepository;
    private final PaymentEventProducer producer;

    public OutboxWorker(
            OutboxRepository outboxRepository,
            PaymentEventProducer producer
    ) {
        this.outboxRepository = outboxRepository;
        this.producer = producer;
    }

    @Scheduled(fixedDelay = 3000)
    @Transactional
    public void processOutbox() {

        Pageable pageable = PageRequest.of(0, BATCH_SIZE);

        List<OutboxEvent> events =
                outboxRepository.findForProcessing(pageable);

        for (OutboxEvent event : events) {
            try {
                producer.send(
                        event.getAggregateId().toString(),
                        event.getPayload()
                );

                event.setStatus(OutboxStatus.SENT.name());
                event.setSentAt(Instant.now());
                log.info("Outbox → Kafka payload: {}", event.getPayload());

            } catch (Exception ex) {

                int retries = event.getRetryCount() + 1;
                event.setRetryCount(retries);

                if (retries >= MAX_RETRIES) {
                    event.setStatus(OutboxStatus.DEAD.name());
                } else {
                    event.setStatus(OutboxStatus.FAILED.name());
                }
            }
        }
    }
}
