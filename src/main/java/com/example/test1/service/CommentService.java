package com.example.test1.service;

import com.example.test1.entity.Comment;
import com.example.test1.exception.ForumOperationException;
import com.example.test1.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Transactional(rollbackFor = Exception.class)
    public Comment addComment(Comment comment, String userId) {
        comment.setCommentId(UUID.randomUUID().toString());
        comment.setUserId(userId);
        comment.setStatus(Comment.Status.NORMAL);
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());

        int rows = commentMapper.insert(comment);
        if (rows != 1) {
            throw new ForumOperationException("评论发布失败");
        }

        return comment;
    }

    public List<Comment> getCommentsByForumId(String forumId) {
        return commentMapper.selectByForumId(forumId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(String commentId) {
        int rows = commentMapper.deleteById(commentId);
        if (rows != 1) {
            throw new ForumOperationException("删除评论失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateComment(Comment comment) {
        comment.setUpdateTime(LocalDateTime.now());
        int rows = commentMapper.updateContent(comment);
        if (rows != 1) {
            throw new ForumOperationException("更新评论失败");
        }
    }
}
