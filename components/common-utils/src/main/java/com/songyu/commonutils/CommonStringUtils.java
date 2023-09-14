package com.songyu.commonutils;

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
        return str == null || str.length() == 0;
    }

    /**
     * 是否不为空串，不为null且长度大于0
     *
     * @param str 字符串
     * @return 是否空串
     */
    public static boolean isNotEmpty(String str) {
        return str != null && str.length() > 0;
    }

    /**
     * 是否为空白串，为null或者去除首尾空白字符后长度为 0
     *
     * @param str 字符串
     * @return 是否空串
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 是否不为空白串，不为null且去除首尾空白字符长度大于0
     *
     * @param str 字符串
     * @return 是否空串
     */
    public static boolean isNotBlank(String str) {
        return str != null && str.trim().length() > 0;
    }


}
