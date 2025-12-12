package com.example.notification_service.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final StringRedisTemplate redis;

    public NotificationService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void saveEvent(String email, String eventJson) {
        String key = "notifications:" + email;

        redis.opsForList().leftPush(key, eventJson);
    }

    public java.util.List<String> getEvents(String email) {
        String key = "notifications:" + email;
        return redis.opsForList().range(key, 0, -1);
    }
}
