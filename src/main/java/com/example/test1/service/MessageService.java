package com.example.test1.service;

import com.example.test1.dto.MessageRequestDTO;
import com.example.test1.dto.MessageResponseDTO;
import com.example.test1.entity.Message;
import com.example.test1.entity.User;
import com.example.test1.mapper.MessageMapper;
import com.example.test1.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;
    public List<Message> selectAll(){
        return messageMapper.selectAll();
    }

    public List<Message> selectBySenderId(String senderId){
        return messageMapper.selectBySenderId(senderId);
    }

    public List<Message> selectByReceiverId(String receiverId){
        return messageMapper.selectByReceiverId(receiverId);
    }

    public MessageResponseDTO sendMessage(String senderId, MessageRequestDTO requestDTO) {
        Optional<User> senderOpt = userMapper.selectByUserId(senderId);
        Optional<User> receiverOpt = userMapper.selectByUserId(requestDTO.getReceiverId());

        if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
            throw new IllegalArgumentException("用户不存在");
        }

        Message message = new Message();
        message.setSender(senderOpt.get());
        message.setReceiver(receiverOpt.get());
        message.setContent(requestDTO.getContent());
        message.setSentAt(LocalDateTime.now());

        Message saved = messageMapper.save(message);
        return toDTO(saved);
    }

    public List<MessageResponseDTO> getMessages(String senderId, String receiverId) {
        User sender = userMapper.selectByUserId(senderId).orElseThrow();
        User receiver = userMapper.selectByUserId(receiverId).orElseThrow();

        return messageMapper
                .findBySenderAndReceiverOrderBySentAtAsc(sender, receiver)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MessageResponseDTO> getInbox(String userId) {
        User user = userMapper.selectByUserId(userId).orElseThrow();
        return messageMapper
                .findByReceiverOrderBySentAtDesc(user)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    private MessageResponseDTO toDTO(Message message) {
        MessageResponseDTO dto = new MessageResponseDTO();
        dto.setId(message.getId());
        dto.setSenderId(message.getSender().getUserId());
        dto.setReceiverId(message.getReceiver().getUserId());
        dto.setContent(message.getContent());
        dto.setSentAt(message.getSentAt());
        return dto;
    }
}
