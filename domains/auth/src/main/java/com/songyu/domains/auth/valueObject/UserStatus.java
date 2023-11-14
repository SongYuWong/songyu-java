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
    BLACK;

    /**
     * 通过枚举序号索引获取用户状态枚举
     *
     * @param code 索引号
     * @return 用户状态枚举
     */
    public static UserStatus mathByCode(Integer code) {
        if (code == null) {
            return null;
        }
        return UserStatus.values()[code];
    }

    /**
     * 状态码是否是注册
     * @param userStatusCode 状态码
     * @return 是否是注册
     */
    public static boolean statusRegistering(int userStatusCode) {
        return REGISTERED.equals(userStatusCode);
    }

    public boolean equals(int code) {
        return this.ordinal() == code;
    }
}
