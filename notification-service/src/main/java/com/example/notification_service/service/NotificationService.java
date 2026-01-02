package com.example.notification_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class NotificationService {

    private static final String DEDUP_KEY = "processed-events";

    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;

    public NotificationService(StringRedisTemplate redis,
                               ObjectMapper objectMapper) {
        this.redis = redis;
        this.objectMapper = objectMapper;
    }

    public void save(String email, Object event) {
        try {
            String key = "notifications:" + email;
            String json = objectMapper.writeValueAsString(event);
            redis.opsForList().leftPush(key, json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean markProcessed(String eventId) {
        Long added = redis.opsForSet().add(DEDUP_KEY, eventId);
        if (added != null && added == 1L) {
            redis.expire(DEDUP_KEY, Duration.ofMinutes(30));
            return true;
        }
        return false;
    }
}
