package com.songyu.components.api.exception;

/**
 * <p>
 * 接口异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 15:18
 */
public abstract class ApiException extends RuntimeException {

    public ApiException() {
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

}
