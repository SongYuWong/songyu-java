package com.songyu.domains.auth.exception;

import com.songyu.domains.auth.valueObject.UserStatus;
import lombok.Getter;

/**
 * <p>
 * 非法的用户状态
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 17:08
 */
public class IllegalUserStatusException extends Exception {

    /**
     * 用户状态
     */
    @Getter
    private final UserStatus userStatus;

    public IllegalUserStatusException(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public IllegalUserStatusException(String message, UserStatus userStatus) {
        super(message);
        this.userStatus = userStatus;
    }

    public IllegalUserStatusException(String message, Throwable cause, UserStatus userStatus) {
        super(message, cause);
        this.userStatus = userStatus;
    }

    public IllegalUserStatusException(Throwable cause, UserStatus userStatus) {
        super(cause);
        this.userStatus = userStatus;
    }

    public IllegalUserStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, UserStatus userStatus) {
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
