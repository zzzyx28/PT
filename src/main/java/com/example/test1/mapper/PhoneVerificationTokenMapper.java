package com.example.test1.mapper;

import com.example.test1.entity.PhoneVerificationToken;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PhoneVerificationTokenMapper {

    @Insert("INSERT INTO phone_verification_token (token, phone, expired_at) VALUES (#{token}, #{phone}, #{expiredAt})")
    void insert(PhoneVerificationToken token);

    @Select("SELECT * FROM phone_verification_token WHERE token = #{token}")
    PhoneVerificationToken findByToken(String token);

    @Update("UPDATE phone_verification_token SET used = true WHERE token = #{token}")
    void markAsUsed(String token);

    @Delete("DELETE FROM phone_verification_token WHERE token = #{token}")
    void deleteByToken(String token);
}
