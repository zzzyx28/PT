package com.example.test1.controller;

import com.example.test1.dto.MessageRequestDTO;
import com.example.test1.dto.MessageResponseDTO;
import com.example.test1.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    // 假设使用JWT或Session后从token中获取 senderId
    private String getCurrentUserId() {
        return "001"; // 示例：假设登录用户ID为1
    }

    @PostMapping("/send")
    public MessageResponseDTO sendMessage(@RequestBody MessageRequestDTO messageDTO) {
        return messageService.sendMessage(getCurrentUserId(), messageDTO);
    }

    @GetMapping("/with/{receiverId}")
    public List<MessageResponseDTO> getConversation(@PathVariable String receiverId) {
        return messageService.getMessages(getCurrentUserId(), receiverId);
    }

    @GetMapping("/inbox")
    public List<MessageResponseDTO> getInbox() {
        return messageService.getInbox(getCurrentUserId());
    }
}
