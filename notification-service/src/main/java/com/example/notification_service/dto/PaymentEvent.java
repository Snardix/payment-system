package com.example.notification_service.dto;

public class PaymentEvent {

    private String eventId;
    private String eventType;
    private String aggregateId;
    private String occurredAt;
    private Payload payload;

    public static class Payload {
        private String email;
        private String transactionId;
        private String status;
        private String amount;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getTransactionId() { return transactionId; }
        public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getAmount() { return amount; }
        public void setAmount(String amount) { this.amount = amount; }
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getAggregateId() { return aggregateId; }
    public void setAggregateId(String aggregateId) { this.aggregateId = aggregateId; }

    public String getOccurredAt() { return occurredAt; }
    public void setOccurredAt(String occurredAt) { this.occurredAt = occurredAt; }

    public Payload getPayload() { return payload; }
    public void setPayload(Payload payload) { this.payload = payload; }
}
