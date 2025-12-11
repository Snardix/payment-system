package com.example.notification_service.kafka;

import com.example.notification_service.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    private final NotificationService repo;

    public NotificationListener(NotificationService repo) {
        this.repo = repo;
    }

    @KafkaListener(topics = "auth-events")
    public void handleRegistrationEvent(String email) {
        repo.saveNotification(email, "Вы успешно зарегистрировались!");
        System.out.println("Уведомление сохранено в Redis для: " + email);
    }
}