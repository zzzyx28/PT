package com.example.test1.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
public class EmailUtils {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtils.class);
    @Autowired
    private JavaMailSender mailSender;

    // 邮箱验证链接模板
    private static final String VERIFICATION_LINK_TEMPLATE = "http://yourdomain.com/api/user/verify-email?token=%s";

    /**
     * 发送邮箱验证邮件
     *
     * @param email 用户邮箱
     */
    public void sendVerificationEmail(String email, String verifyLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("【PT站】请验证你的邮箱");
            message.setText("欢迎注册！请点击以下链接验证邮箱地址：%n%n" + verifyLink);
            mailSender.send(message);
            logger.info("验证码邮件已发送至：{}", email);
        } catch (Exception e) {
            logger.error("发送邮件失败", e);
            throw new RuntimeException("邮件发送失败：" + e.getMessage());
        }
    }
    /**
     * 发送密码重置邮件
     *
     * @param email 用户邮箱
     * @param token 密码重置 Token
     */
    public void sendPasswordResetEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("【PT站】密码重置请求");
        message.setText(String.format("你正在申请重置密码，请点击以下链接继续操作：%n%n" +
                "http://yourdomain.com/reset-password?token=%s", token));
        mailSender.send(message);
    }
}
