package com.songyu.commonutils.exception;

/**
 * <p>
 * 处理来源空异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/12 18:16
 */
public class SourceNullException extends CommonUtilException {

    public SourceNullException() {
        super("处理来源空异常");
    }

    public SourceNullException(String message) {
        super(message);
    }

    public SourceNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public SourceNullException(Throwable cause) {
        super(cause);
    }

    public SourceNullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
