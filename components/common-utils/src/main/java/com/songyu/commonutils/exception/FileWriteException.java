package com.songyu.commonutils.exception;

/**
 * <p>
 * 文件输出异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/13 11:27
 */
public class FileWriteException extends CommonUtilException{
    public FileWriteException() {
    }

    public FileWriteException(String message) {
        super(message);
    }

    public FileWriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileWriteException(Throwable cause) {
        super(cause);
    }

    public FileWriteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
