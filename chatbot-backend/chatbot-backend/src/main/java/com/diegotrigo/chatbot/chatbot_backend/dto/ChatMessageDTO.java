package com.diegotrigo.chatbot.chatbot_backend.dto;

public class ChatMessageDTO {
    private String userId;
    private String message;

    public ChatMessageDTO() {}

    public ChatMessageDTO(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
