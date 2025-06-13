package com.example.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.test1.entity.InvitationCode;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface InvitationCodeMapper extends BaseMapper<InvitationCode> {

    @Select("SELECT * FROM invitation_code WHERE code = #{code}")
    InvitationCode findByCode(String code);
    @Select("SELECT * FROM invitation_code ORDER BY created_at DESC")
    List<InvitationCode> selectCodeList();
    @Update("UPDATE invitation_code SET status = 'USED', used_by = #{usedBy}, used_at = NOW() WHERE code = #{code}")
    int useCode(String code, String usedBy);




}
