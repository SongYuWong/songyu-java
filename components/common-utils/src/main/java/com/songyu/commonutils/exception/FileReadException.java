package com.songyu.commonutils.exception;

/**
 * <p>
 * 文件读取异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/13 11:04
 */
public class FileReadException extends CommonUtilException {
    public FileReadException() {
    }

    public FileReadException(String message) {
        super(message);
    }

    public FileReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileReadException(Throwable cause) {
        super(cause);
    }

    public FileReadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
