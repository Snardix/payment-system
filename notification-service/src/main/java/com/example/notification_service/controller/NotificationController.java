package com.example.notification_service.controller;

import com.example.notification_service.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    /*@GetMapping("/{email}")
    public List<String> getNotifications(@PathVariable String email) {
        return service.getEvents(email);
    }*/
}
