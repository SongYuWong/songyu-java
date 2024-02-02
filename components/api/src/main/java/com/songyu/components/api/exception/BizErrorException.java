package com.songyu.components.api.exception;

/**
 * <p>
 * 业务异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 15:20
 */
public class BizErrorException extends ApiException{

    public BizErrorException() {
    }

    public BizErrorException(String message) {
        super(message);
    }

    public BizErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
