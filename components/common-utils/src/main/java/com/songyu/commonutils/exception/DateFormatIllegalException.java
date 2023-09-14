package com.songyu.commonutils.exception;

/**
 * <p>
 * 非法的日期格式
 * </p>
 *
 * @author songYu
 * @since 2023/9/13 15:40
 */
public class DateFormatIllegalException extends CommonUtilException {
    public DateFormatIllegalException() {
    }

    public DateFormatIllegalException(String message) {
        super(message);
    }

    public DateFormatIllegalException(String message, Throwable cause) {
        super(message, cause);
    }

    public DateFormatIllegalException(Throwable cause) {
        super(cause);
    }

    public DateFormatIllegalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
