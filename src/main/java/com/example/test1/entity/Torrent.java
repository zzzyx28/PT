package com.example.test1.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@TableName("torrent")
public class Torrent {
    // 数据库字段（严格对应）
    @TableId(value = "torrentId", type = IdType.ASSIGN_UUID)
    private String torrentId;

    @TableField("name")
    private String name;

    @TableField("filename")
    private String filename;

    @TableField("title")
    private String title;

    @TableField("description")
    private String description;

    @TableField("status")
    private Integer status;

    @TableField("createtime")
    private String createTime;

    @TableField("updateTime")
    private String updateTime;

    @TableField("ownerId")
    private String ownerId;

    @TableField("type")
    private Integer type;

    @TableField("comments")
    private Integer comments;

    @TableField("views")
    private Integer views;

    @TableField("leechers")
    private Integer leechers;

    @TableField("seeders")
    private Integer seeders;

    @TableField("completions")
    private Integer completions;

    // 非数据库字段（业务逻辑需要）
    @TableField(exist = false)
    private byte[] infoHash;

    @TableField(exist = false)
    private Long size;

    @TableField(exist = false)
    private Integer fileCount;

    // 状态常量
    public interface Status {
        int CANDIDATE = 0;
        int PUBLISHED = 1;
        int AUDIT_NOT_PASSED = 2;
        int RETRIAL = 3;
        int REMOVED = 10;
    }

    // Getter 和 Setter 方法
    public String getTorrentId() {
        return torrentId;
    }

    public void setTorrentId(String torrentId) {
        this.torrentId = torrentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getLeechers() {
        return leechers;
    }

    public void setLeechers(Integer leechers) {
        this.leechers = leechers;
    }

    public Integer getSeeders() {
        return seeders;
    }

    public void setSeeders(Integer seeders) {
        this.seeders = seeders;
    }

    public Integer getCompletions() {
        return completions;
    }

    public void setCompletions(Integer completions) {
        this.completions = completions;
    }

    public byte[] getInfoHash() {
        return infoHash;
    }

    public void setInfoHash(byte[] infoHash) {
        this.infoHash = infoHash;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getFileCount() {
        return fileCount;
    }

    public void setFileCount(Integer fileCount) {
        this.fileCount = fileCount;
    }

    // 工具方法：设置时间（转换LocalDateTime为String）
    public void setCreateTime(LocalDateTime time) {
        this.createTime = time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public void setUpdateTime(LocalDateTime time) {
        this.updateTime = time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}