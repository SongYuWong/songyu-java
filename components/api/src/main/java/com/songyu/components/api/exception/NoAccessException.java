package com.songyu.components.api.exception;

/**
 * <p>
 * 无权限异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 15:22
 */
public class NoAccessException extends ApiException {

    public NoAccessException() {
    }

    public NoAccessException(String message) {
        super(message);
    }

    public NoAccessException(String message, Throwable cause) {
        super(message, cause);
    }

}
