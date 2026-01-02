package com.example.payment_service.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public PaymentEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String key, String payload) {
        try {
            kafkaTemplate.send("payment-events", key, payload).get();
        } catch (Exception e) {
            throw new RuntimeException("Kafka send failed", e);
        }
    }
}