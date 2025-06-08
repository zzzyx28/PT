package com.example.test1.service;

import com.example.test1.entity.*;
import com.example.test1.exception.UserException;
import com.example.test1.mapper.EmailVerificationTokenMapper;
import com.example.test1.mapper.InvitationCodeMapper;
import com.example.test1.mapper.PhoneVerificationTokenMapper;
import com.example.test1.mapper.UserMapper;
import com.example.test1.util.EmailUtils;
import com.example.test1.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PhoneVerificationTokenMapper phoneVerificationTokenMapper;
    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private InvitationCodeMapper invitationCodeMapper;

    @Autowired
    private EmailVerificationTokenMapper emailVerificationTokenMapper;

    // 注册用户（带邀请码）
    @Transactional(rollbackFor = Exception.class)
    public User register(String username, String email, String password, String inviteCode, String phone) {
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
        user.setMagic_value(0);

        userMapper.insert(user);
        // 发送手机验证码
        sendPhoneVerificationCode(phone);

//        // 生成 Token 并发送验证邮件
//        String token = UUID.randomUUID().toString();
//        EmailVerificationToken verificationToken = new EmailVerificationToken();
//        verificationToken.setToken(token);
//        verificationToken.setUserId(user.getUserId());
//        verificationToken.setEmail(email);
//        verificationToken.setExpiredAt(LocalDateTime.now().plusHours(24));
//
//        emailVerificationTokenMapper.insert(verificationToken);
//
//        String verifyLink = "http://localhost:5173/api/user/verify-email?token=" + token;


////        -----------------网络问题先注释-----------------
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

    @Transactional(rollbackFor = Exception.class)
    public void verifyPhone(String token) {
        PhoneVerificationToken verificationToken = phoneVerificationTokenMapper.findByToken(token);

        if (verificationToken == null || verificationToken.isUsed()) {
            throw new UserException("无效或已使用的验证码");
        }

        if (LocalDateTime.now().isAfter(verificationToken.getExpiredAt())) {
            phoneVerificationTokenMapper.deleteByToken(token);
            throw new UserException("验证码已过期，请重新获取");
        }

        User user = userMapper.selectByPhone(verificationToken.getPhone());
        if (user == null) {
            throw new UserException("用户不存在");
        }

        if (user.getIsPhoneVerified() == 1) {
            throw new UserException("该手机号已验证");
        }

        user.setIsPhoneVerified(1);
        userMapper.updatePhoneAndVerificationStatus(user);

        phoneVerificationTokenMapper.markAsUsed(token);
    }

    // 发送手机验证码
    public void sendPhoneVerificationCode(String phone) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5); // 验证码过期时间设为5分钟

        PhoneVerificationToken verificationToken = new PhoneVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setPhone(phone);
        verificationToken.setExpiredAt(expiredAt);
        verificationToken.setUsed(false);

        phoneVerificationTokenMapper.insert(verificationToken);

        // 模拟发送短信验证码逻辑，实际应调用短信服务接口
        System.out.println("发送至 " + phone + " 的验证码是：" + token);
    }


    // 登录验证
    public User login(String email, String password) {
        User user = userMapper.selectByEmail(email);

        //  ------------测试-----------
        user.setIs_email_verified(1);

        if (user.getIsBanned() == 1) {
            throw new UserException("该账号已被封禁，请联系管理员");
        }


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
    @Transactional(rollbackFor = Exception.class)
    public void addExperience(String userId, long amount) {
        userMapper.addExperience(userId, amount);
    }

    // 增加魔力值
    @Transactional(rollbackFor = Exception.class)
    public void addMagicValue(String userId, int amount) {
        userMapper.addMagicValue(userId, amount);
    }

    public User getUserById(String userId) {
        return userMapper.selectByUserId(userId);
    }

    /**
     * 封禁用户账号
     */
    @Transactional(rollbackFor = Exception.class)
    public void banUser(String userId) {
        if (userMapper.checkIfBanned(userId) == 1) {
            throw new UserException("该账号已被封禁");
        }
        userMapper.updateBanStatus(userId, 1); // 设置为封禁状态
    }

    /**
     * 解禁用户账号
     */
    @Transactional(rollbackFor = Exception.class)
    public void unbanUser(String userId) {
        if (userMapper.checkIfBanned(userId) == 0) {
            throw new UserException("该账号未被封禁");
        }
        userMapper.updateBanStatus(userId, 0); // 设置为非封禁状态
    }

    /**
     * 创建新的邀请码
     */
    @Transactional(rollbackFor = Exception.class)
    public String createInvitationCode(String creatorId) {
        InvitationCode.Status  status = InvitationCode.Status.ACTIVE;

        String code = generateRandomCode(8); // 生成随机邀请码

        InvitationCode invitationCode = new InvitationCode();
        invitationCode.setCode(code);
        invitationCode.setCreatorId(creatorId);
        invitationCode.setStatus(status); // 初始状态为 ACTIVE

        invitationCodeMapper.insert(invitationCode);

        return code;
    }


    /**
     * 生成指定长度的随机邀请码（字母+数字）
     */
    private String generateRandomCode(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            code.append(chars.charAt(index));
        }
        return code.toString();
    }

    /**
     * 根据用户名查询用户信息
     */
    public User getUserByUsername(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new UserException("用户不存在");
        }
        return user;
    }

    // 获取按等级排行的用户
    public List<User> getTopUsersByLevel() {
        return userMapper.findTopUsersByLevel();
    }

    // 获取按下载量排行的用户
    public List<User> getTopUsersByDownloadCount() {
        return userMapper.findTopUsersByDownloadCount();
    }

    public List<Torrent> getTopTorrentsByDownload() {
        return userMapper.findTopTorrentsByDownload();
    }

    public List<User> getAllUsers() {
        return userMapper.findAllUsers();
    }
    /**
     * 修改用户等级
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserLevel(String userId, Integer newLevel) {
        User user = userMapper.selectByUserId(userId);
        if (user == null) {
            throw new UserException("用户不存在");
        }
        user.setLevel(newLevel);
        userMapper.updateById(user);
    }
}
