package com.example.test1.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@TableName("comment")
public class Comment {

    @TableId(value = "comment_id")
    private String commentId;

    @TableField("forum_id")
    private String forumId;

    @TableField("user_id")
    private String userId;

    private String content;

    @TableField("create_time")
    private String createTime;

    @TableField("update_time")
    private String updateTime;

    private Integer status; // 0: normal, 1: deleted

    public interface Status {
        int NORMAL = 0;
        int DELETED = 1;
    }

    // Getter and Setter methods...

    public String getCommentId() { return commentId; }
    public void setCommentId(String commentId) { this.commentId = commentId; }

    public String getForumId() { return forumId; }
    public void setForumId(String forumId) { this.forumId = forumId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }

    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public void setCreateTime(LocalDateTime time) {
        this.createTime = time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public void setUpdateTime(LocalDateTime time) {
        this.updateTime = time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
