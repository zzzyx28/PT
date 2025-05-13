package com.example.test1.dto;

import java.time.LocalDateTime;

public class MessageResponseDTO {
    private String messageId;
    private String senderId;
    private String receiverId;
    private String content;
    private LocalDateTime sentAt;

    // Getters & Setters
    public String getId() { return messageId; }
    public void setId(String messageId) { this.messageId = messageId; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}