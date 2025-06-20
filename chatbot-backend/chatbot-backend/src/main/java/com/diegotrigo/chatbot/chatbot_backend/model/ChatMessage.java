package com.diegotrigo.chatbot.chatbot_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String message;

    private boolean fromUser;

    private LocalDateTime timestamp;

    public ChatMessage() {}

    public ChatMessage(String userId, String message, boolean fromUser, LocalDateTime timestamp) {
        this.userId = userId;
        this.message = message;
        this.fromUser = fromUser;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isFromUser() { return fromUser; }
    public void setFromUser(boolean fromUser) { this.fromUser = fromUser; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
