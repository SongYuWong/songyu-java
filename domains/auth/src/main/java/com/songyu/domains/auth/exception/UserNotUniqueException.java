package com.songyu.domains.auth.exception;

import com.songyu.domains.auth.entity.User;

/**
 * <p>
 * 用户信息不唯一异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 11:20
 */
public class UserNotUniqueException extends Exception {

    /**
     * 用户信息
     */
    private final User user;

    public UserNotUniqueException(User user) {
        this.user = user;
    }

    public UserNotUniqueException(String message, User user) {
        super(message);
        this.user = user;
    }

    public UserNotUniqueException(String message, Throwable cause, User user) {
        super(message, cause);
        this.user = user;
    }

    public UserNotUniqueException(Throwable cause, User user) {
        super(cause);
        this.user = user;
    }

    public UserNotUniqueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, User user) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.user = user;
    }

}
