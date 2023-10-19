package com.songyu.components.jsonweb.exception;

/**
 * <p>
 * 无效的使用方
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 15:59
 */
public class InvalidAudienceException extends Exception {

    public InvalidAudienceException() {
    }

    public InvalidAudienceException(String message) {
        super(message);
    }

    public InvalidAudienceException(String message, Throwable cause) {
        super(message, cause);
    }
}
