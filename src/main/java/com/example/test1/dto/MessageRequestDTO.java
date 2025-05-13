package com.example.test1.dto;


public class MessageRequestDTO {
    private String receiverId;
    private String content;

    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}