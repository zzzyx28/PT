//package com.example.test1.mapper;
//
//import com.example.test1.entity.Message;
//import com.example.test1.entity.User;
//import org.apache.ibatis.annotations.*;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
////@Mapper
//public interface MessageMapper extends JpaRepository<Message, Long> {
//    @Select("select * from message")
//    List<Message> selectAll();
//
//    @Select("select * from message where senderId = #{senderId}")
//    List<Message> selectBySenderId(String senderId);
//
//    @Select("select * from message where receiverId = #{receiverId}")
//    List<Message> selectByReceiverId(String receiverId);
//
//    @Delete("delete from message where messageId = #{messageId}")
//    void deleteMessage(String messageId);
//
//    List<Message> findBySenderAndReceiverOrderBySentAtAsc(User sender, User receiver);
//
//    List<Message> findByReceiverOrderBySentAtDesc(User receiver);
//}
