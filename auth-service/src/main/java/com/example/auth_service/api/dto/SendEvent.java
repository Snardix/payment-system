package com.example.auth_service.api.dto;

public class SendEvent {
    private String email;
    private String type;
    private long timestamp;

    public SendEvent() {}

    public SendEvent(String email, String type, long timestamp) {
        this.email = email;
        this.type = type;
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
