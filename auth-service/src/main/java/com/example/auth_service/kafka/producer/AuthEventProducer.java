package com.example.auth_service.kafka.producer;

import com.example.auth_service.api.dto.RegistrationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AuthEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public AuthEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendRegistrationEvent(String email) {
        RegistrationEvent event = new RegistrationEvent(
                email,
                "REGISTER",
                System.currentTimeMillis()
        );

        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("auth-events", json);
            System.out.println("Kafka: отправлено событие → " + json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации события", e);
        }
    }
}
