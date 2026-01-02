package com.example.notification_service.kafka;

import com.example.notification_service.dto.GetEvent;
import com.example.notification_service.dto.NotificationEvent;
import com.example.notification_service.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static org.apache.kafka.common.requests.DeleteAclsResponse.log;

@Service
public class NotificationListener {

    private final NotificationService notificationService;

    public NotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "auth-events")
    public void handleEvent(String json) {
        try {
            GetEvent event = new ObjectMapper().readValue(json, GetEvent.class);

            NotificationEvent notification = new NotificationEvent(
                    event.getType(),
                    String.valueOf(event.getTimestamp()),
                    null
            );

            notificationService.save(event.getEmail(), notification);

        } catch (Exception e) {
            log.error("Auth event error", e);
        }
    }
}
