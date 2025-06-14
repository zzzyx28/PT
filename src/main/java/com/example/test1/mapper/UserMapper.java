package com.example.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.test1.entity.Ranking;
import com.example.test1.entity.Torrent;
import com.example.test1.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE email = #{email}")
    User selectByEmail(String email);

    @Select("SELECT * FROM user WHERE user_id = #{username}")
    User selectByUsername(String username);

    @Select("SELECT * FROM user WHERE user_id = #{userId}")
    User selectByUserId(String userId);

    @Select("SELECT * FROM user WHERE invite_code = #{inviteCode}")
    User selectByInviteCode(String inviteCode);

    @Insert("INSERT INTO user (user_id, username, email, password, invite_code, created_at) " +
            "VALUES (#{userId}, #{username}, #{email}, #{password}, #{inviteCode}, NOW())")
    int insert(User user);

    @Update("UPDATE user SET password = #{password} WHERE username = #{username}")
    int updatePasswordByUserName(@Param("username") String username, @Param("password") String password);

    @Update("UPDATE user SET avatar_url = #{avatarUrl}, signature = #{signature}, username=#{username}, bio = #{bio} WHERE user_id = #{userId}")
    int updateProfileByUserId(User user);

    @Update("UPDATE user SET bio = #{bio} WHERE username = #{username}")
    int updateBioByUsername(User user);

    @Update("UPDATE user SET experience = experience + #{amount}, level = FLOOR((experience + #{amount}) / 1000) + 1 WHERE user_id = #{userId}")
    int addExperience(@Param("userId") String userId, @Param("amount") long amount);

    @Update("UPDATE user SET magic_value = magic_value + #{amount} WHERE user_id = #{userId}")
    int addMagicValue(@Param("userId") String userId, @Param("amount") int amount);

    @Update("UPDATE user SET is_email_verified = true WHERE user_id = #{userId}")
    int verifyEmail(String userId);

    @Update("UPDATE user SET phone = #{phone}, is_phone_verified = #{isPhoneVerified} WHERE user_id = #{userId}")
    int updatePhoneAndVerificationStatus(User user);
    @Select("SELECT * FROM user WHERE phone = #{phone}")
    User selectByPhone(String phone);

    @Update("UPDATE user SET is_banned = #{status} WHERE user_id = #{userId}")
    int updateBanStatus(@Param("userId") String userId, @Param("status") int status);

    @Select("SELECT is_banned FROM user WHERE user_id = #{userId}")
    int checkIfBanned(String userId);

    // 按用户等级排行
    @Select("SELECT username, magic_value, level FROM user")
    List<User> findTopUsersByLevel();

    // 按种子下载量排行
    @Select("SELECT t.ownerId AS userId, u.username, SUM(t.completions) AS downloadCount " +
            "FROM torrent t " +
            "JOIN user u ON t.ownerId = u.user_id " +
            "GROUP BY t.ownerId, u.username " +
            "ORDER BY downloadCount DESC LIMIT 10")
    List<User> findTopUsersByDownloadCount();


    @Select("SELECT t.ownerId AS userId, t.name, u.username, t.completions AS downloadCount " +
            "FROM torrent t " +
            "JOIN user u ON t.ownerId = u.user_id " +
            "ORDER BY downloadCount DESC " +
            "LIMIT 10")
    List<Torrent> findTopTorrentsByDownload();
    @Select("SELECT * FROM user")
    List<User> findAllUsers();
}
