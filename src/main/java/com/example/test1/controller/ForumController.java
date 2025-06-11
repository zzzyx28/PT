package com.example.test1.controller;

import com.example.test1.entity.Forum;
import com.example.test1.exception.ForumOperationException;
import com.example.test1.service.ForumService;
import com.example.test1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;
    @Autowired
    private UserService userService;
    @PostMapping("/create")
    public ResponseEntity<?> createForum(@RequestBody Forum forum, Principal principal) {
        try {
            // 测试阶段临时指定一个 ownerId
            String ownerId = Optional.ofNullable(principal)
                    .map(Principal::getName)
                    .orElse("1234");  // 如果 principal 为 null，默认使用 testUserId
            // 获取当前登录用户的 ID
//                    .orElseThrow(() -> new ForumOperationException("用户未登录"));
            Forum created = forumService.createForum(forum, ownerId);
            userService.addExperience(ownerId,100);
            userService.addMagicValue(ownerId,100);
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
    @GetMapping("/listByCategory")
    public ResponseEntity<?> listForumsByCategory(
            @RequestParam(value = "category") int category,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        try {
            return ResponseEntity.ok(forumService.listForumsByCategory(category, page, size));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("获取帖子列表失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteForum(@RequestParam String forumId) {
        try {
            forumService.deleteForum(forumId);
            return ResponseEntity.ok("帖子删除成功");
        } catch (ForumOperationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
