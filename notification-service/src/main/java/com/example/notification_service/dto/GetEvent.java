package com.example.notification_service.dto;

public class GetEvent {
    private String email;
    private String type;
    private long timestamp;

    public GetEvent() {}

    public String getEmail() { return email; }
    public String getType() { return type; }
    public long getTimestamp() { return timestamp; }

    public void setEmail(String email) { this.email = email; }
    public void setType(String type) { this.type = type; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
