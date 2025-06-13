package com.example.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.test1.entity.Forum;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ForumMapper extends BaseMapper<Forum> {

    @Delete("DELETE FROM forum WHERE forumId = #{forumId}")
    int deleteById(String forumId);

    @Select("SELECT * FROM forum WHERE forumId = #{forumId}")
    Forum selectById(String forumId);

    @Select("SELECT * FROM forum ORDER BY create_time DESC LIMIT #{offset}, #{limit}")
    List<Forum> selectListWithPagination(int offset, int limit);

    @Select("SELECT * FROM forum")
    Forum selectAll();

    @Select("SELECT * FROM forum where category = #{category}")
    Forum selectByCategory(String category);

    @Select("SELECT f.*, COUNT(c.comment_id) AS comments FROM forum f LEFT JOIN comment c ON f.forumId = c.forum_id GROUP BY f.forumId")
    List<Forum> selectWithCommentCount();

    @Insert({
            "<script>",
            "INSERT INTO forum (forumId, title, content, status, category, create_time, update_time, owner_id)",
            "VALUES (#{forumId}, #{title}, #{content}, #{status}, #{category}, #{createTime}, #{updateTime}, #{ownerId})",
            "</script>"
    })
    int insertForum(Forum forum);
}
