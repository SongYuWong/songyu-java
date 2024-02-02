package com.songyu.commonutils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

/**
 * <p>
 * 通用集合工具类
 * </p>
 *
 * @author songYu
 * @since 2023/9/22 18:16
 */
public class CommonCollectionUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }
}
