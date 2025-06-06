package com.example.test1.controller;

import com.example.test1.entity.Comment;
import com.example.test1.exception.ForumOperationException;
import com.example.test1.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/comment")
@CrossOrigin(origins = "http://localhost:5173")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestBody Comment comment, Principal principal) {
        try {
            String userId = Optional.ofNullable(principal)
                    .map(Principal::getName)
                    .orElse("1234");// 测试阶段临时指定一个 ownerId
            // 获取当前登录用户的 ID
//                    .orElseThrow(() -> new ForumOperationException("用户未登录"));

            Comment created = commentService.addComment(comment, userId);
            return ResponseEntity.ok(created);
        } catch (ForumOperationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("服务器错误: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> listComments(@RequestParam String forumId) {
        try {
            return ResponseEntity.ok(commentService.getCommentsByForumId(forumId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("获取评论失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteComment(@RequestParam String commentId) {
        try {
            commentService.deleteComment(commentId);
            return ResponseEntity.ok("评论删除成功");
        } catch (ForumOperationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateComment(@RequestBody Comment comment) {
        try {
            commentService.updateComment(comment);
            return ResponseEntity.ok("评论更新成功");
        } catch (ForumOperationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
