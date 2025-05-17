package com.example.test1.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    /**
     * 对明文密码进行加密
     *
     * @param rawPassword 明文密码
     * @return 加密后的密码
     */
    public static String hash(String rawPassword) {
        // 使用默认的 salt 生成器，强度为 12
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(12));
    }

    /**
     * 校验明文密码与加密后的密码是否匹配
     *
     * @param rawPassword 明文密码
     * @param hashedPassword 数据库存储的加密密码
     * @return 是否匹配
     */
    public static boolean check(String rawPassword, String hashedPassword) {
        if (hashedPassword == null || rawPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(rawPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
