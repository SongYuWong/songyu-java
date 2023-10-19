package com.songyu.domains.auth.exception;

import cn.hutool.json.JSONUtil;
import com.songyu.domains.auth.entity.User;

/**
 * <p>
 * 用户信息不合法异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 11:26
 */
public class IllegalUserInfoException extends Exception {

    /**
     * 用户信息
     */
    private final User user;

    public IllegalUserInfoException(User user) {
        this.user = user;
    }

    public IllegalUserInfoException(String message, User user) {
        super(message);
        this.user = user;
    }

    public IllegalUserInfoException(String message, Throwable cause, User user) {
        super(message, cause);
        this.user = user;
    }

    public IllegalUserInfoException(Throwable cause, User user) {
        super(cause);
        this.user = user;
    }

    public IllegalUserInfoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, User user) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.user = user;
    }

    @Override
    public String toString() {
        return "IllegalUserInfoException{" +
                "user=" + JSONUtil.toJsonStr(user) +
                '}';
    }
}
