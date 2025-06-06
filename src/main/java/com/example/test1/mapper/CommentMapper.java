package com.example.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.test1.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    @Insert("INSERT INTO comment(comment_id, forum_id, user_id, content, create_time, update_time, status) " +
            "VALUES(#{commentId}, #{forumId}, #{userId}, #{content}, NOW(), NOW(), #{status})")
    int insert(Comment comment);

    @Select("SELECT * FROM comment WHERE forum_id = #{forumId} AND status = 0 ORDER BY create_time DESC")
    List<Comment> selectByForumId(String forumId);

    @Delete("DELETE FROM comment WHERE comment_id = #{commentId}")
    int deleteById(String commentId);

    @Update("UPDATE comment SET content = #{content}, update_time = NOW() WHERE comment_id = #{commentId}")
    int updateContent(Comment comment);
}
