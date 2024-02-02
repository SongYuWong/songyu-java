package com.songyu.components.springboot.mvc.utils;

import com.songyu.commonutils.CommonParamCheckUtils;
import com.songyu.commonutils.exception.SourceNullException;
import com.songyu.components.springboot.mvc.exception.IllegalRequestParamException;

/**
 * <p>
 * 请求参数校验工具
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 11:33
 */
public class RequestParamChecker {

    /**
     * 参数不为 null
     *
     * @param object  参数对象
     * @param message 为空时异常信息
     */
    public static void notNull(Object object, String message) {
        try {
            CommonParamCheckUtils.notNull(object, message);
        } catch (SourceNullException e) {
            throw new IllegalRequestParamException(e.getMessage(), object);
        }
    }

}
