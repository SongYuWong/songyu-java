package com.songyu.commonutils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 通用字符串工具类
 * </p>
 *
 * @author songYu
 * @since 2023/9/12 15:59
 */
public class CommonStringUtils {

    /**
     * 是否为空串，为null或者长度为0
     *
     * @param str 字符串
     * @return 是否空串
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 是否不为空串，不为null且长度大于0
     *
     * @param str 字符串
     * @return 是否空串
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    /**
     * 是否为空白串，为null或者去除首尾空白字符后长度为 0
     *
     * @param str 字符串
     * @return 是否空串
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 是否不为空白串，不为null且去除首尾空白字符长度大于0
     *
     * @param str 字符串
     * @return 是否空串
     */
    public static boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * 字符串数组中是否有任意一个是空白字符串
     *
     * @param strs 字符串数组
     * @return 是否有任意一个是空白字符串
     */
    public static boolean anyIsBlank(String... strs) {
        for (String str : strs) {
            if (isBlank(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 字符串数组中是否所有都是空白字符串
     *
     * @param strs 字符串数组
     * @return 是否有任意一个是空白字符串
     */
    public static boolean allIsBlank(String... strs) {
        for (String str : strs) {
            if (isNotBlank(str)) {
                return false;
            }
        }
        return true;
    }

    public static String encode(String str, String charset) throws UnsupportedEncodingException {
        return new String(str.getBytes("GBK"), charset);
    }

    public static boolean isValidEmail(String email) {
        // 定义邮箱正则表达式
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        // 创建 Pattern 对象
        Pattern pattern = Pattern.compile(emailRegex);
        // 创建 Matcher 对象
        Matcher matcher = pattern.matcher(email);
        // 判断是否匹配
        return matcher.matches();
    }

}
