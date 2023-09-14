package com.songyu.commonutils;

import com.songyu.commonutils.exception.SourceNullException;

/**
 * <p>
 * 通用参数校验工具
 * </p>
 *
 * @author songYu
 * @since 2023/9/12 18:19
 */
public class CommonParamCheckUtils {

    public static void notNull(Object o, String message) throws SourceNullException {
        if (o == null) {
            throw new SourceNullException(message);
        }
    }

}
