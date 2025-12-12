package com.example.notification_service.dto;

public class RegistrationEvent {
    private String email;
    private String type;
    private long timestamp;

    public RegistrationEvent() {}

    public String getEmail() { return email; }
    public String getType() { return type; }
    public long getTimestamp() { return timestamp; }

    public void setEmail(String email) { this.email = email; }
    public void setType(String type) { this.type = type; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
