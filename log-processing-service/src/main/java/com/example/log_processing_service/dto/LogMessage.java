package com.example.log_processing_service.dto;

public class LogMessage {
    private String level;
    private String message;
    private String timestamp;
    private String serviceName;

    // Constructors
    public LogMessage() {
    }

    public LogMessage(String level, String message, String timestamp, String serviceName) {
        this.level = level;
        this.message = message;
        this.timestamp = timestamp;
        this.serviceName = serviceName;
    }

    // Getters and Setters
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "LogMessage{" +
                "level='" + level + '\'' +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }
}