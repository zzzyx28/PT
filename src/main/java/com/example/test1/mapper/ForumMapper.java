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
}
