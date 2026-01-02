package com.example.notification_service.dto;

public class NotificationEvent {

    private String type;
    private String timestamp;
    private Object payload;

    public NotificationEvent() {}

    public NotificationEvent(String type, String timestamp, Object payload) {
        this.type = type;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    public String getType() { return type; }
    public String getTimestamp() { return timestamp; }
    public Object getPayload() { return payload; }
}
