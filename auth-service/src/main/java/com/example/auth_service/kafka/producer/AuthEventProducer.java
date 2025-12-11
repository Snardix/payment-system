package com.example.auth_service.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AuthEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public AuthEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendRegistrationEvent(String email) {
        kafkaTemplate.send("auth-events", email);
        System.out.println("Kafka: отправлено сообщение о регистрации → " + email);
    }
}
