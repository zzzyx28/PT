package com.example.test1.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zyx
 * @email
 * @date 2025-04-20
 */
@Data
@TableName("comment")
public class Comment {
    @TableId
    private String CommentId;
    @Schema(description = "LikesNumber")
    private int LikesNumber;

    private String content;

    private LocalDateTime CommentPostingTime;

    private String postUser;

    private String getCommentId() {
        return CommentId;
    }

    private String getContent() {
        return content;
    }

    private int getLikesNumber() {
        return LikesNumber;
    }

}
