package com.example.notification_service.kafka;

import com.example.notification_service.dto.RegistrationEvent;
import com.example.notification_service.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    private final NotificationService notificationService;

    public NotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "auth-events")
    public void handleRegistrationEvent(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            RegistrationEvent event = mapper.readValue(json, RegistrationEvent.class);

            notificationService.saveEvent(event.getEmail(), json);

            System.out.println("Redis: сохранено событие для: " + event.getEmail());
        } catch (Exception e) {
            System.out.println("Ошибка обработки Kafka JSON: " + e.getMessage());
        }
    }
}
