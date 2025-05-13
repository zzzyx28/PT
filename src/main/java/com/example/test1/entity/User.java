package com.example.test1.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
//import com.rocketpt.server.common.exception.RocketPTException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author zyx
 * @email
 * @date 2025-04-20
 */
@Entity
@Table(name = "user")
public class User {
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getUploaded() {
        return uploaded;
    }

    public void setUploaded(Long uploaded) {
        this.uploaded = uploaded;
    }

    public Long getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(Long downloaded) {
        this.downloaded = downloaded;
    }

    public Long getBonus() {
        return bonus;
    }

    public void setBonus(Long bonus) {
        this.bonus = bonus;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    @Id
    private String userId;
    /**
     * 账户名
     */
    @Schema(description = "userName")
    private String userName;
    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;
    /**
     * 注册时间
     */
    @Schema(description = "注册时间")
    private LocalDateTime createTime;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;
    /**
     * 性别 0男 1女 2其他
     */
    @Schema(description = "性别 0男 1女 2其他")
    private Integer gender;
    /**
     * 状态 0正常 1 已锁定 2未激活
     */
    @Schema(description = "状态 0正常 1 已锁定 2未激活")
    private Integer state;
    /**
     * 邮件地址
     */
    @Schema(description = "email")
    private String email;

    /**
     * 管理备注
     */
    @Schema(description = "管理备注")
    private String remark;
    /**
     * 上次登录时间
     */
    @Schema(description = "上次登录时间")
    private LocalDateTime lastLogin;
    /**
     * 上次访问时间
     */
    @Schema(description = "上次访问时间")
    private LocalDateTime lastAccess;

    /**
     * 上次发布offer时间
     */
    private LocalDateTime lastOffer;

    /**
     * 隐私级别 0 1 2
     */
    @Schema(description = "隐私级别")
    private Integer privacy;
    /**
     * 用户等级
     */
    private Integer level;
    /**
     * 用户最大等级
     */
    private Integer levelMax;
    /**
     * 上传量
     */
    @Schema(description = "上传量")
    private Long uploaded;
    /**
     * 下载量
     */
    @Schema(description = "下载量")
    private Long downloaded;
    /**
     * 真实上传量
     */
    @Schema(description = "真实上传量")
    private Long realUploaded;
    /**
     * 真实下载量
     */
    @Schema(description = "真实下载量")
    private Long realDownloaded;
    /**
     * 做种时间
     */
    @Schema(description = "做种时间")
    private Long seedTime;
    /**
     * 下载时间
     */
    @Schema(description = "下载时间")
    private Long leechTime;

    /**
     *
     */
    private Integer download;
    /**
     *
     */
    private Integer upload;
    /**
     * 上家
     */
    @Schema(description = "上家ID")
    private Integer inviter;
    /**
     * 魔力积分
     */
    @Schema(description = "魔力积分")
    private Long bonus;
    /**
     * 经验值
     */
    @Schema(description = "经验值")
    private Long exp;


}
