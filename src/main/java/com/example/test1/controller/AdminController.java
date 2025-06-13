package com.example.test1.controller;

import com.example.test1.entity.InvitationCode;
import com.example.test1.entity.User;
import com.example.test1.exception.UserException;
import com.example.test1.mapper.InvitationCodeMapper;
import com.example.test1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private InvitationCodeMapper invitationCodeMapper;

    @PostMapping("invite-code/create")
    public ResponseEntity<?> createInviteCode(
            @RequestParam String creatorId) {
        String code = generateRandomCode(); // 如 INVITE123456

        InvitationCode invitationCode = new InvitationCode();
        invitationCode.setCode(code);
        invitationCode.setCreatorId(creatorId);

        invitationCodeMapper.insert(invitationCode);
        return ResponseEntity.ok().body("邀请码生成成功: " + code);
    }
    @GetMapping("/invite-code/list")
    public ResponseEntity<?> listInviteCodes() {
        try {
            List<InvitationCode> codes = invitationCodeMapper.selectCodeList();
            return ResponseEntity.ok(codes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("获取邀请码列表失败: " + e.getMessage());
        }
    }

    private String generateRandomCode() {
        // 示例：生成8位大写随机码
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return sb.toString();
    }

    @PostMapping("/ban")
    public ResponseEntity<?> banUser(@RequestParam String userId) {
        try {
            userService.banUser(userId);
            return ResponseEntity.ok("账号已封禁");
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/unban")
    public ResponseEntity<?> unbanUser(@RequestParam String userId) {
        try {
            userService.unbanUser(userId);
            return ResponseEntity.ok("账号已解禁");
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/update-level")
    public ResponseEntity<?> updateLevel(@RequestParam String userId, @RequestParam Integer level) {
        try {
            userService.updateUserLevel(userId, level);
            return ResponseEntity.ok("用户等级已更新");
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/invite-code/buy/{userId}")
    public ResponseEntity<?> buy(@PathVariable String userId) {
        try {
            User user=userService.getUserByUsername(userId);
            System.out.println("**********");
            System.out.println(user);
            int magic=user.getMagic_value();
            String id=user.getUserId();
            System.out.println(magic);
            if(magic>=50){
                userService.addMagicValue(id,-50);
                String code=generateRandomCode();
                InvitationCode invitationCode = new InvitationCode();
                invitationCode.setCode(code);
                invitationCode.setCreatorId(userId);
                invitationCodeMapper.insert(invitationCode);
                return ResponseEntity.ok().body("购买邀请码成功 " + code);
            }
            else{
                return ResponseEntity.ok("魔力值不足");
            }

        } catch (UserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
