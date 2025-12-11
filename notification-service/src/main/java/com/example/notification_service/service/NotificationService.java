package com.example.notification_service.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final StringRedisTemplate redis;

    public NotificationService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void saveNotification(String email, String message) {
        redis.opsForValue().set(email, message);
    }

    public String getNotification(String email) {
        return redis.opsForValue().get(email);
    }
}