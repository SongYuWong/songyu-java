package com.songyu.domains.auth.valueObject;

/**
 * <p>
 * 用户状态枚举
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 14:36
 */
public enum UserStatus {

    /**
     * 注册用户
     */
    REGISTERED,

    /**
     * 普通用户
     */
    NORMAL,

    /**
     * 特殊用户
     */
    VIP,

    /**
     * 黑名单用户
     */
    BLACK
}
