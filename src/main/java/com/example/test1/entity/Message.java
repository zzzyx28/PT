package com.example.test1.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ManyToAny;

import java.time.LocalDateTime;

@Entity
public class Message {
    @Id
    private String messageId;
    @ManyToOne
    private User sender;
    @ManyToOne
    private User receiver;

    private String senderId;

    private String receiverId;

    private String content;

    private LocalDateTime sentAt;

    // Getters & Setters
    public String getId() {
        return messageId;
    }

    public void setId(String messageId) {
        this.messageId = messageId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentTime) {
        this.sentAt = sentTime;
    }

}
