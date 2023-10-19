package com.songyu.components.springboot.mvc.exception;

import cn.hutool.json.JSONUtil;

/**
 * <p>
 * 非法的请求参数异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 11:34
 */
public class IllegalRequestParamException extends RuntimeException {

    private final Object param;

    public IllegalRequestParamException(Object param) {
        this.param = param;
    }

    public IllegalRequestParamException(String message, Object param) {
        super(message);
        this.param = param;
    }

    public IllegalRequestParamException(String message, Throwable cause, Object param) {
        super(message, cause);
        this.param = param;
    }

    public IllegalRequestParamException(Throwable cause, Object param) {
        super(cause);
        this.param = param;
    }

    public IllegalRequestParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object param) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.param = param;
    }

    @Override
    public String toString() {
        return "IllegalRequestParamException{" +
                "param=" + JSONUtil.toJsonStr(param) +
                '}';
    }

}
