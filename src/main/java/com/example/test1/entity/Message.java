package com.example.test1.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@TableName("private_message")
public class Message {

    @TableId(value = "message_id", type = IdType.ASSIGN_UUID)
    private String messageId;

    @TableField("sender_id")
    private String senderId;

    @TableField("receiver_id")
    private String receiverId;

    private String content;

    @TableField("create_time")
    private String createTime;

    @TableField("is_read")
    private Boolean isRead;

    // Getter and Setter...

    public void setCreateTime(LocalDateTime time) {
        this.createTime = time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
}
