package com.example.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.test1.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 自定义查询所有用户
    @Select("SELECT * FROM user")
    List<User> selectAll();

    // 自定义根据 userId 查询
    @Select("SELECT * FROM user WHERE userId = #{userId}")
    User selectByUserId(String userId);
}
