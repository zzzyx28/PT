package com.example.test1.controller;

import com.example.test1.entity.User;
import com.example.test1.exception.UserException;
import com.example.test1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 注册

    // 注册接口保持不变
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String inviteCode) {
        try {
            User user = userService.register(username, email, password, inviteCode);
            return ResponseEntity.ok("注册成功，请查收邮箱验证邮件");
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 邮箱验证接口
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        try {
            userService.verifyEmail(token);
            return ResponseEntity.ok("邮箱验证成功，账户已激活！");
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // 登录
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {

        try {
            User user = userService.login(email, password);
            return ResponseEntity.ok(user);
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 修改密码
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
//            @RequestParam String userName,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            Principal principal) {
        try {

            String userName = Optional.ofNullable(principal)
                    .map(p -> p.getName())
                    .orElse("test1");

            userService.changePassword(userName, oldPassword, newPassword);
            return ResponseEntity.ok("密码修改成功");
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 修改资料
    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody User user) {
        try {
            userService.updateProfile(user);
            return ResponseEntity.ok("资料更新成功");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("资料更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/update-bio")
    public ResponseEntity<?> updateBio(@RequestBody User user) {
        try {
            userService.updateBio(user);
            return ResponseEntity.ok("个人简介更新成功");
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // 获取用户信息
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(userService.getUserById(userId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("获取用户信息失败: " + e.getMessage());
        }
    }

}
