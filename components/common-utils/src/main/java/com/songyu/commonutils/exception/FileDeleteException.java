package com.songyu.commonutils.exception;

/**
 * <p>
 * 文件删除异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/12 17:56
 */
public class FileDeleteException extends CommonUtilException {

    public FileDeleteException() {
        super("文件删除异常");
    }

    public FileDeleteException(String message) {
        super(message);
    }

    public FileDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileDeleteException(Throwable cause) {
        super(cause);
    }

    public FileDeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
