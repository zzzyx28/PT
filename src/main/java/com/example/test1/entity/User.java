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
    @TableField("magic_value")
    private int magicValue;

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

    public int getMagic_value() {
        return magicValue;
    }

    public void setMagic_value(int magic_value) {
        this.magicValue = magic_value;
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

    private int isBanned; // 0: 未封禁, 1: 已封禁

    public int getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(int isBanned) {
        this.isBanned = isBanned;
    }
    private String phone;
    private int isPhoneVerified;

    // Getter and Setter...
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIsPhoneVerified() {
        return isPhoneVerified;
    }

    public void setIsPhoneVerified(int isPhoneVerified) {
        this.isPhoneVerified = isPhoneVerified;
    }
}


