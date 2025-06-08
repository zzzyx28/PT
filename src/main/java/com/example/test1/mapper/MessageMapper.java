package com.example.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.test1.entity.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {

    @Insert("INSERT INTO private_message (message_id, sender_id, receiver_id, content, create_time, is_read) " +
            "VALUES (#{messageId}, #{senderId}, #{receiverId}, #{content}, #{createTime}, #{isRead})")
    int insertMessage(Message message);

    @Select("SELECT * FROM private_message WHERE receiver_id = #{userId} ORDER BY create_time DESC LIMIT #{offset}, #{limit}")
    List<Message> selectByReceiverId(@Param("userId") String userId, @Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT * FROM private_message WHERE sender_id = #{userId} OR receiver_id = #{userId} ORDER BY create_time DESC LIMIT #{offset}, #{limit}")
    List<Message> selectByUserId(@Param("userId") String userId, @Param("offset") int offset, @Param("limit") int limit);

    @Update("UPDATE private_message SET is_read = true WHERE message_id = #{messageId}")
    int markAsRead(@Param("userId") String userId, @Param("messageId") String messageId);
}
