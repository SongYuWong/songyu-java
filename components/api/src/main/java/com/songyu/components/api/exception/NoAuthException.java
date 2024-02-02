package com.songyu.components.api.exception;

/**
 * <p>
 * 无认证异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 15:21
 */
public class NoAuthException extends ApiException {

    public NoAuthException() {
    }

    public NoAuthException(String message) {
        super(message);
    }

    public NoAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
