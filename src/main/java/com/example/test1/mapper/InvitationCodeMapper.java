package com.example.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.test1.entity.InvitationCode;
import org.apache.ibatis.annotations.*;

@Mapper
public interface InvitationCodeMapper extends BaseMapper<InvitationCode> {

    @Select("SELECT * FROM invitation_code WHERE code = #{code}")
    InvitationCode selectByCode(String code);

    @Update("UPDATE invitation_code SET status = 'USED', used_by = #{userId}, used_at = NOW() WHERE code = #{code}")
    int markAsUsed(@Param("code") String code, @Param("userId") String userId);

    @Insert("INSERT INTO invitation_code (code, creator_id, status) VALUES (#{code}, #{creatorId}, 'ACTIVE')")
    int insert(InvitationCode code);
}
