package com.songyu.commonutils.exception;

/**
 * <p>
 * 文件创建异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/12 22:05
 */
public class FileCreateException extends CommonUtilException {

    public FileCreateException() {
        super("文件创建异常");
    }

    public FileCreateException(String message) {
        super(message);
    }

    public FileCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileCreateException(Throwable cause) {
        super(cause);
    }

    public FileCreateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
