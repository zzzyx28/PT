package com.example.test1.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@TableName("forum")
public class Forum {
    @TableId(value = "forumId")
    private String forumId;

    private String title;
    private String content;
    private Integer status;

    private int category;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    @TableField("create_time")
    private String createTime;

    @TableField("update_time")
    private String updateTime;

    @TableField("owner_id")
    private String ownerId;

    private Integer comments;

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }
    private Integer views;
    private Integer replies;

    // Getter and Setter methods...
    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
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

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getReplies() {
        return replies;
    }

    public void setReplies(Integer replies) {
        this.replies = replies;
    }

    public void setCreateTime(LocalDateTime time) {
        this.createTime = time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public void setUpdateTime(LocalDateTime time) {
        this.updateTime = time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    // 状态常量
    public interface Status {
        int DRAFT = 0;
        int PUBLISHED = 1;
        int DELETED = 2;
    }
}
