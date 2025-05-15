package com.example.test1.controller;

import com.example.test1.entity.Message;
import com.example.test1.exception.MessageOperationException;
import com.example.test1.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody Message message, Principal principal) {
        try {
            String senderId = Optional.ofNullable(principal)
                    .map(Principal::getName)
                    .orElse("testUserId");

            Message sent = messageService.sendMessage(message, senderId);
            return ResponseEntity.ok(sent);
        } catch (MessageOperationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("服务器错误: " + e.getMessage());
        }
    }

    @GetMapping("/received")
    public ResponseEntity<?> getReceivedMessages(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Principal principal) {
        try {
            String userId = Optional.ofNullable(principal)
                    .map(Principal::getName)
                    .orElse("User2");

            return ResponseEntity.ok(messageService.getMessagesByReceiver(userId, page, size));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("获取私信失败: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllMessages(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Principal principal) {
        try {
            String userId = Optional.ofNullable(principal)
                    .map(Principal::getName)
                    .orElse("testUserId");

            return ResponseEntity.ok(messageService.getAllMessages(userId, page, size));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("获取私信失败: " + e.getMessage());
        }
    }

    @PostMapping("/mark-read")
    public ResponseEntity<?> markAsRead(@RequestParam String messageId, Principal principal) {
        try {
            String userId = Optional.ofNullable(principal)
                    .map(Principal::getName)
                    .orElse("testUserId");

            messageService.markMessageAsRead(userId, messageId);
            return ResponseEntity.ok("标记为已读成功");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("操作失败: " + e.getMessage());
        }
    }
}
