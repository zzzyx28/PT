package com.example.test1.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {

    @TableId(type = IdType.INPUT) // 若 userId 是由你控制（如UUID），使用 INPUT；自增用 AUTO
    private String userId;

    @Schema(description = "userName")
    private String userName;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "注册时间")
    private LocalDateTime createTime;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "性别 0男 1女 2其他")
    private Integer gender;

    @Schema(description = "状态 0正常 1已锁定 2未激活")
    private Integer state;

    @Schema(description = "email")
    private String email;

    @Schema(description = "管理备注")
    private String remark;

    @Schema(description = "上次登录时间")
    private LocalDateTime lastLogin;

    @Schema(description = "上次访问时间")
    private LocalDateTime lastAccess;

    private LocalDateTime lastOffer;

    @Schema(description = "隐私级别")
    private Integer privacy;

    private Integer level;

    private Integer levelMax;

    @Schema(description = "上传量")
    private Long uploaded;

    @Schema(description = "下载量")
    private Long downloaded;

    @Schema(description = "真实上传量")
    private Long realUploaded;

    @Schema(description = "真实下载量")
    private Long realDownloaded;

    @Schema(description = "做种时间")
    private Long seedTime;

    @Schema(description = "下载时间")
    private Long leechTime;

    private Integer download;

    private Integer upload;

    @Schema(description = "上家ID")
    private Integer inviter;

    @Schema(description = "魔力积分")
    private Long bonus;

    @Schema(description = "经验值")
    private Long exp;
}
