package com.example.test1.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@TableName("announcement")
public class Announcement {

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    private String title;
    private String content;

    @TableField("create_time")
    private String createTime;

    @TableField("update_time")
    private String updateTime;

    @TableField("publisher_id")
    private String publisherId;

    private Integer status;

    // Getter and Setter...
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCreateTime(LocalDateTime time) {
        this.createTime = time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public void setUpdateTime(LocalDateTime time) {
        this.updateTime = time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    // 状态常量
    public interface Status {
        int DRAFT = 1;
        int PUBLISHED = 2;
        int ARCHIVED = 3;
    }
}
