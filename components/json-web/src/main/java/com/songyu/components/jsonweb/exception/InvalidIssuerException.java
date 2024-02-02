package com.songyu.components.jsonweb.exception;

/**
 * <p>
 * 无效的构建者异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 15:58
 */
public class InvalidIssuerException extends Exception {

    public InvalidIssuerException() {
    }

    public InvalidIssuerException(String message) {
        super(message);
    }

    public InvalidIssuerException(String message, Throwable cause) {
        super(message, cause);
    }

}
