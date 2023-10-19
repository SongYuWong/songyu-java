package com.songyu.domains.auth.exception;

/**
 * <p>
 * 用户已经存在
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 11:53
 */
public class UserAlreadyExist extends Exception {

    /**
     * 已经存在的用户编码
     */
    private final String userCode;

    public UserAlreadyExist(String userCode) {
        this.userCode = userCode;
    }

    public UserAlreadyExist(String message, String userCode) {
        super(message);
        this.userCode = userCode;
    }

    public UserAlreadyExist(String message, Throwable cause, String userCode) {
        super(message, cause);
        this.userCode = userCode;
    }

    public UserAlreadyExist(Throwable cause, String userCode) {
        super(cause);
        this.userCode = userCode;
    }

    public UserAlreadyExist(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String userCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.userCode = userCode;
    }

    @Override
    public String toString() {
        return "UserAlreadyExist{" +
                "userCode='" + userCode + '\'' +
                '}';
    }
}
