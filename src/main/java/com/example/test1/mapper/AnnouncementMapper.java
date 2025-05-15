package com.example.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.test1.entity.Announcement;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {

    @Insert("INSERT INTO announcement (id, title, content, create_time, update_time, publisher_id, status) " +
            "VALUES (#{id}, #{title}, #{content}, #{createTime}, #{updateTime}, #{publisherId}, #{status})")
    int insertAnnouncement(Announcement announcement);

    @Select("SELECT * FROM announcement ORDER BY create_time DESC LIMIT #{offset}, #{limit}")
    List<Announcement> selectPageList(@Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT * FROM announcement WHERE id = #{id}")
    Announcement selectById(String id);

    @Update("UPDATE announcement SET title = #{title}, content = #{content}, update_time = NOW(), status = #{status} WHERE id = #{id}")
    int updateAnnouncement(Announcement announcement);

    @Delete("DELETE FROM announcement WHERE id = #{id}")
    int deleteById(String id);
    @Select("SELECT * FROM announcement WHERE title LIKE CONCAT('%', #{keyword}, '%') ORDER BY create_time DESC LIMIT #{offset}, #{limit}")
    List<Announcement> selectByTitleLike(@Param("keyword") String keyword,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);
    @Select("SELECT COUNT(*) FROM announcement WHERE title LIKE CONCAT('%', #{keyword}, '%')")
    long countByTitleLike(String keyword);

}
