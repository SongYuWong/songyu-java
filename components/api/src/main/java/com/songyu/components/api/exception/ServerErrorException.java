package com.songyu.components.api.exception;

/**
 * <p>
 * 响应错误异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 15:20
 */
public class ServerErrorException extends ApiException {

    public ServerErrorException() {
    }

    public ServerErrorException(String message) {
        super(message);
    }

    public ServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
