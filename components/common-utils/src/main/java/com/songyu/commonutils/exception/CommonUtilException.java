package com.songyu.commonutils.exception;

/**
 * <p>
 * 通用工具类异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/12 16:12
 */
public abstract class CommonUtilException extends Exception {
    public CommonUtilException() {
    }

    public CommonUtilException(String message) {
        super(message);
    }

    public CommonUtilException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonUtilException(Throwable cause) {
        super(cause);
    }

    public CommonUtilException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
