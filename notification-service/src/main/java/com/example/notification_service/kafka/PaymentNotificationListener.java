package com.example.notification_service.kafka;

import com.example.notification_service.dto.NotificationEvent;
import com.example.notification_service.dto.PaymentEvent;
import com.example.notification_service.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class PaymentNotificationListener {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public PaymentNotificationListener(
            NotificationService notificationService,
            ObjectMapper objectMapper
    ) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "payment-events", groupId = "notification-payment-group")
    public void handlePaymentEvent(String json) throws JsonProcessingException {
        try {
            PaymentEvent event =
                    objectMapper.readValue(json, PaymentEvent.class);

            if (!notificationService.markProcessed(event.getEventId())) {
                return;
            }

            NotificationEvent notification = new NotificationEvent(
                    event.getEventType(),
                    event.getOccurredAt(),
                    event.getPayload()
            );

            notificationService.save(
                    event.getPayload().getEmail(),
                    notification
            );

        } catch (Exception e) {
            throw e;
        }
    }
}
