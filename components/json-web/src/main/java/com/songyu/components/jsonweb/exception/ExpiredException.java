package com.songyu.components.jsonweb.exception;

/**
 * <p>
 * 过期异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 15:53
 */
public class ExpiredException extends Exception {

    public ExpiredException() {
    }

    public ExpiredException(String message) {
        super(message);
    }

    public ExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

}
