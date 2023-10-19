package com.songyu.components.jsonweb.exception;

/**
 * <p>
 * 验证签名失败
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 16:01
 */
public class InvalidSignatureException extends Exception {

    public InvalidSignatureException() {
    }

    public InvalidSignatureException(String message) {
        super(message);
    }

    public InvalidSignatureException(String message, Throwable cause) {
        super(message, cause);
    }
}
