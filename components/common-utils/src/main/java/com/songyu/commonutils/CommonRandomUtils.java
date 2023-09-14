package com.songyu.commonutils;

import com.songyu.commonutils.exception.SourceStringBlankException;

import java.security.SecureRandom;

/**
 * <p>
 * 通用随机工具类
 * </p>
 *
 * @author songYu
 * @since 2023/9/12 16:09
 */
public class CommonRandomUtils {

    /**
     * 随机 len 长度的字符串
     * <p>
     * 随机范围：qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890!@#$%^&*()_+-=,./'
     * </p>
     *
     * @param len 长度
     * @return len 长度的随机字符串
     */
    public static String randomString(int len) {
        try {
            return randomString("qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890!@#$%^&*()_+-=,./'", len);
        } catch (SourceStringBlankException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从给出的字符范围串中随机出固定长度的字符串
     *
     * @param seeds 字符范围串
     * @param len   随机长度
     * @return 随机结果
     * @throws SourceStringBlankException 字符范围串为空串
     */
    public static String randomString(String seeds, int len) throws SourceStringBlankException {
        if (CommonStringUtils.isBlank(seeds)) {
            throw new SourceStringBlankException("随机字符不能为空字符");
        }
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(seeds.charAt(secureRandom.nextInt(seeds.length())));
        }
        return builder.toString();
    }

}
