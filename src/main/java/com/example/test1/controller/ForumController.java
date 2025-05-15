package com.example.test1.controller;

import com.example.test1.entity.Forum;
import com.example.test1.exception.ForumOperationException;
import com.example.test1.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;

    @PostMapping("/create")
    public ResponseEntity<?> createForum(@RequestBody Forum forum, Principal principal) {
        try {
            // 测试阶段临时指定一个 ownerId
            String ownerId = Optional.ofNullable(principal)
                    .map(Principal::getName)
                    .orElse("testUserId");  // 如果 principal 为 null，默认使用 testUserId

            Forum created = forumService.createForum(forum, ownerId);
            return ResponseEntity.ok(created);
        } catch (ForumOperationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("服务器错误: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> listForums(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            return ResponseEntity.ok(forumService.listForums(page, size));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("获取帖子列表失败: " + e.getMessage());
        }
    }
}
