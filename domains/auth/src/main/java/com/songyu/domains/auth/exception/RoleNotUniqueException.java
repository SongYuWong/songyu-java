package com.songyu.domains.auth.exception;

import com.songyu.domains.auth.entity.Role;

/**
 * <p>
 * 角色信息不唯一异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 11:20
 */
public class RoleNotUniqueException extends Exception {

    /**
     * 角色信息
     */
    private final Role role;

    public RoleNotUniqueException(Role role) {
        this.role = role;
    }

    public RoleNotUniqueException(String message, Role role) {
        super(message);
        this.role = role;
    }

    public RoleNotUniqueException(String message, Throwable cause, Role role) {
        super(message, cause);
        this.role = role;
    }

    public RoleNotUniqueException(Throwable cause, Role role) {
        super(cause);
        this.role = role;
    }

    public RoleNotUniqueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Role role) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.role = role;
    }

}
