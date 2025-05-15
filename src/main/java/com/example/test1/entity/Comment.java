//package com.example.test1.entity;
//
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import io.swagger.v3.oas.annotations.media.Schema;
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.Data;
//
//import java.time.LocalDateTime;
//
///**
// * @author zyx
// * @email
// * @date 2025-04-20
// */
//
//
//@Data
//@TableName("comment")
//
//
//public class Comment {
//    @Id
//    private String commentId;
//    @Schema(description = "LikesNumber")
//    private int likesNumber;
//
//    private String content;
//
//    private LocalDateTime commentAt;
//
//    private String postUser;
//
//    public String getCommentId() {
//        return commentId;
//    }
//
//    public void setCommentId(String commentId) {
//        this.commentId = commentId;
//    }
//
//    public int getLikesNumber() {
//        return likesNumber;
//    }
//
//    public void setLikesNumber(int likesNumber) {
//        this.likesNumber = likesNumber;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public LocalDateTime getCommentAt() {
//        return commentAt;
//    }
//
//    public void setCommentAt(LocalDateTime commentAt) {
//        this.commentAt = commentAt;
//    }
//
//    public String getPostUser() {
//        return postUser;
//    }
//
//    public void setPostUser(String postUser) {
//        this.postUser = postUser;
//    }
//}
