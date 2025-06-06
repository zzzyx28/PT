package com.example.test1.util;

import org.springframework.stereotype.Component;

@Component
public class SmsUtils {

    public void sendVerificationCode(String phone) {
        // 调用短信平台发送验证码逻辑
        System.out.println("发送至：" + phone + " 的验证码");
    }
}
