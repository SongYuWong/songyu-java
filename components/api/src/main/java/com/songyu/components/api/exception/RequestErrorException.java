package com.songyu.components.api.exception;

/**
 * <p>
 * 错误请求异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 15:19
 */
public class RequestErrorException extends ApiException {

    public RequestErrorException() {
    }

    public RequestErrorException(String message) {
        super(message);
    }

    public RequestErrorException(String message, Throwable cause) {
        super(message, cause);
    }

}
