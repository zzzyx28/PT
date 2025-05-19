package com.example.test1.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

@TableName("user")
public class User {

    @TableId(value = "user_id", type = IdType.ASSIGN_UUID)
    private String userId;

    private String username;
    private String email;
    private String password;

    private String avatarUrl;
    private String signature;

    private Long experience;
    private Integer level;
    private Integer magicValue;

    private int is_email_verified;

    @TableField("invite_code")
    private String inviteCode;

    @TableField("created_at")
    private LocalDateTime createdAt;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    private String bio;

    // Getter and Setter...

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getExperience() {
        return experience;
    }

    public void setExperience(Long experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getMagicValue() {
        return magicValue;
    }

    public void setMagicValue(Integer magicValue) {
        this.magicValue = magicValue;
    }

    public int getIs_email_verified() {
        return is_email_verified;
    }

    public void setIs_email_verified(int is_email_verified) {
        this.is_email_verified = is_email_verified;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int isEmailVerified() {
        return is_email_verified;
    }
}
