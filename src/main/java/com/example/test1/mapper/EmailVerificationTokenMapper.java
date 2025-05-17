package com.example.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.test1.entity.EmailVerificationToken;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EmailVerificationTokenMapper extends BaseMapper<EmailVerificationToken> {

    @Insert("INSERT INTO email_verification_token (token, user_id, email, expired_at) VALUES (#{token}, #{userId}, #{email}, #{expiredAt})")
    int insert(EmailVerificationToken token);

    @Select("SELECT * FROM email_verification_token WHERE token = #{token}")
    EmailVerificationToken findByToken(String token);

    @Delete("DELETE FROM email_verification_token WHERE token = #{token}")
    void deleteByToken(String token);
}
