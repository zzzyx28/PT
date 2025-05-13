package com.example.test1.mapper;

import com.example.test1.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    @Select("select * from user")
    List<User> selectAll();

    @Select("select * from user where userId = #{userId}")
    Optional<User> selectByUserId(String userId);
}

