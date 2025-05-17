package com.example.test1.controller;

import com.example.test1.entity.InvitationCode;
import com.example.test1.mapper.InvitationCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/invite-code")
public class AdminInviteCodeController {

    @Autowired
    private InvitationCodeMapper invitationCodeMapper;

    @PostMapping("/create")
    public ResponseEntity<?> createInviteCode(
            @RequestParam String creatorId) {
        String code = generateRandomCode(); // 如 INVITE123456

        InvitationCode invitationCode = new InvitationCode();
        invitationCode.setCode(code);
        invitationCode.setCreatorId(creatorId);

        invitationCodeMapper.insert(invitationCode);
        return ResponseEntity.ok().body("邀请码生成成功: " + code);
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
}
