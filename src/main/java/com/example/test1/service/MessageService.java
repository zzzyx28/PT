package com.example.test1.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.test1.entity.Message;
import com.example.test1.exception.MessageOperationException;
import com.example.test1.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Transactional(rollbackFor = Exception.class)
    public Message sendMessage(Message message, String senderId, String receiverId) {
        message.setMessageId(java.util.UUID.randomUUID().toString());
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setCreateTime(LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        message.setIsRead(false);

        int rows = messageMapper.insertMessage(message);
        if (rows != 1) {
            throw new MessageOperationException("发送私信失败");
        }

        return message;
    }

    public List<Message> getMessagesByReceiver(String receiverId, int page, int size) {
        int offset = (page - 1) * size;
        return messageMapper.selectByReceiverId(receiverId, offset, size);
    }

    public List<Message> getAllMessages(String userId, int page, int size) {
        int offset = (page - 1) * size;
        return messageMapper.selectByUserId(userId, offset, size);
    }

    public void markMessageAsRead(String userId, String messageId) {
        messageMapper.markAsRead(userId, messageId);
    }
}
