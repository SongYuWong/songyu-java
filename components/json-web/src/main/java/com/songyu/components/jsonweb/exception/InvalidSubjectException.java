package com.songyu.components.jsonweb.exception;

/**
 * <p>
 * 无效主题异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 15:55
 */
public class InvalidSubjectException extends Exception {

    public InvalidSubjectException() {
    }

    public InvalidSubjectException(String message) {
        super(message);
    }

    public InvalidSubjectException(String message, Throwable cause) {
        super(message, cause);
    }

}
