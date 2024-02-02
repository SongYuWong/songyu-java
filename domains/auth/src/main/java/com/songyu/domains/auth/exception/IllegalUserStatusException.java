package com.songyu.domains.auth.exception;

import lombok.Getter;

/**
 * <p>
 * 非法的用户状态
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 17:08
 */
@Getter
public class IllegalUserStatusException extends Exception {

    /**
     * 用户状态
     */
    private final Integer userStatus;

    public IllegalUserStatusException(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public IllegalUserStatusException(String message, Integer userStatus) {
        super(message);
        this.userStatus = userStatus;
    }

    public IllegalUserStatusException(String message, Throwable cause, Integer userStatus) {
        super(message, cause);
        this.userStatus = userStatus;
    }

    public IllegalUserStatusException(Throwable cause, Integer userStatus) {
        super(cause);
        this.userStatus = userStatus;
    }

    public IllegalUserStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Integer userStatus) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.userStatus = userStatus;
    }

    @Override
    public String toString() {
        return "IllegalUserStatusException{" +
                "userStatus=" + userStatus +
                '}';
    }
}
