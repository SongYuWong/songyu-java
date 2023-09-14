package com.songyu.commonutils.exception;

/**
 * <p>
 * 源字符串空白异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/12 16:16
 */
public class SourceStringBlankException extends SourceNullException {

    public SourceStringBlankException() {
        super("源字符串空白异常");
    }

    public SourceStringBlankException(String message) {
        super(message);
    }

    public SourceStringBlankException(String message, Throwable cause) {
        super(message, cause);
    }

    public SourceStringBlankException(Throwable cause) {
        super(cause);
    }

    public SourceStringBlankException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
