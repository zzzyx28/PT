package com.example.test1.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName("torrent") // MyBatis-Plus 表名注解
public class Torrent {
    /**
     * 种子ID（主键）
     */
    @TableId(value = "torrent_id", type = IdType.ASSIGN_UUID) // MyBatis-Plus 主键策略
    private String torrentId;

    /**
     * 种子哈希（数据库字段类型应为 BINARY(20)）
     */
    private byte[] infoHash;

    /**
     * 关联的拥有者对象（非数据库字段）
     */

    /**
     * 数据库中的拥有者ID（与 Owner 对象关联）
     */
    @TableField("owner_id") // 指定数据库字段名
    private String ownerId;

    /**
     * 其他字段保持不变，仅调整注解...
     */
    private String name;
    private String filename;
    private String title;
    private String subheading;
    private String cover;
    private String description;
    private Integer category;

    /**
     * 状态（使用枚举或直接映射）
     */
    private Integer status;

    /**
     * 自动填充时间字段配置
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 其他字段保持不变...
    private Integer fileStatus;
    private Integer reviewer;
    private Long size;
    private Integer type;
    private Integer fileCount;
    private Integer comments;
    private Integer views;
    private Integer hits;
    private Integer visible;
    private Integer anonymous;
    private Integer leechers;
    private Integer seeders;
    private Integer completions;
    private String remark;

    /**
     * 状态常量（建议改用枚举）
     */
    public interface Status {
        int CANDIDATE = 0;
        int PUBLISHED = 1;
        int AUDIT_NOT_PASSED = 2;
        int RETRIAL = 3;
        int REMOVED = 10;
    }

    /**
     * 类型枚举（启用并适配 MyBatis-Plus）
     */
    @Getter
    @RequiredArgsConstructor
    public enum FileType {
        SINGLE(1, "单文件"),
        MULTI(2, "多文件");

        @EnumValue // 标记存入数据库的值
        private final int code;
        private final String desc;
    }
}