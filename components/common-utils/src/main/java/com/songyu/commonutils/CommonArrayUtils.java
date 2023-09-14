package com.songyu.commonutils;

/**
 * <p>
 * 通用数组工具类
 * </p>
 *
 * @author songYu
 * @since 2023/9/13 9:11
 */
public class CommonArrayUtils {

    /**
     * 是否是空的数组，为 null 或者大小为 0
     *
     * @return 是否为空
     */
    public static boolean isEmpty(Object[] objects) {
        return objects == null || objects.length == 0;
    }

    /**
     * 是否不是空的数组，不为 null 并且长度大于 0
     *
     * @return 是否不为空
     */
    public static boolean isNotEmpty(Object[] objects) {
        return objects != null && objects.length > 0;
    }

}
