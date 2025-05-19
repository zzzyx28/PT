package com.example.test1.service;

import com.example.test1.entity.EmailVerificationToken;
import com.example.test1.entity.User;
import com.example.test1.exception.UserException;
import com.example.test1.mapper.EmailVerificationTokenMapper;
import com.example.test1.mapper.InvitationCodeMapper;
import com.example.test1.mapper.UserMapper;
import com.example.test1.util.EmailUtils;
import com.example.test1.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private InvitationCodeMapper invitationCodeMapper;

    @Autowired
    private EmailVerificationTokenMapper emailVerificationTokenMapper;

    // 注册用户（带邀请码）
    @Transactional(rollbackFor = Exception.class)
    public User register(String username, String email, String password, String inviteCode) {
        if (userMapper.selectByUsername(username) != null) {
            throw new UserException("用户名已存在");
        }
        if (userMapper.selectByEmail(email) != null) {
            throw new UserException("邮箱已被注册");
        }

        // 校验邀请码...

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(PasswordUtils.hash(password));


//        user.setEmailVerified(0); // 注册后邮箱未验证
        user.setIs_email_verified(1);

        user.setInviteCode(inviteCode);
        user.setLevel(1);
        user.setExperience(0L);
        user.setMagicValue(0);

        userMapper.insert(user);

        // 生成 Token 并发送验证邮件
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUserId(user.getUserId());
        verificationToken.setEmail(email);
        verificationToken.setExpiredAt(LocalDateTime.now().plusHours(24));

        emailVerificationTokenMapper.insert(verificationToken);

        String verifyLink = "http://localhost:5173/api/user/verify-email?token=" + token;


        //-----------------网络问题先注释-----------------
//        emailUtils.sendVerificationEmail(email, verifyLink);

        return user;
    }

    // 邮箱验证
    @Transactional(rollbackFor = Exception.class)
    public void verifyEmail(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenMapper.findByToken(token);

        if (verificationToken == null) {
            throw new UserException("验证链接无效或已过期");
        }

        if (LocalDateTime.now().isAfter(verificationToken.getExpiredAt())) {
            emailVerificationTokenMapper.deleteByToken(token);
            throw new UserException("验证链接已过期，请重新注册");
        }

        User user = userMapper.selectById(verificationToken.getUserId());
        if (user == null) {
            throw new UserException("用户不存在");
        }

        if (user.isEmailVerified()==1) {
            emailVerificationTokenMapper.deleteByToken(token);
            throw new UserException("该邮箱已验证");
        }

        user.setIs_email_verified(1);
        userMapper.updateById(user);

        emailVerificationTokenMapper.deleteByToken(token);
    }


    // 登录验证
    public User login(String email, String password) {
        User user = userMapper.selectByEmail(email);

        //  ------------测试-----------
        user.setIs_email_verified(1);

        if (user == null || !PasswordUtils.check(password, user.getPassword())) {
            throw new UserException("邮箱或密码错误");
        }

        if (user.isEmailVerified() != 1) {
            throw new UserException("邮箱尚未验证，请先完成邮箱验证  "+user.getEmail()+"  "+user.isEmailVerified());
        }

        return user;
    }

    // 修改密码
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(String userName, String oldPassword, String newPassword) {
        User user = userMapper.selectByUsername(userName);
        if (user == null || !PasswordUtils.check(oldPassword, user.getPassword())) {
            throw new UserException("旧密码错误");
        }
        userMapper.updatePasswordByUserName(userName, PasswordUtils.hash(newPassword));
    }

    // 修改资料
    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(User user) {
        userMapper.updateProfileByUserId(user);
    }

    //更新简介
    @Transactional(rollbackFor = Exception.class)
    public void updateBio(User user) {
        userMapper.updateBioByUsername(user);
    }


    // 增加经验
    public void addExperience(String userId, long amount) {
        userMapper.addExperience(userId, amount);
    }

    // 增加魔力值
    public void addMagicValue(String userId, int amount) {
        userMapper.addMagicValue(userId, amount);
    }

    public User getUserById(String userId) {
        return userMapper.selectByUserId(userId);
    }
}
