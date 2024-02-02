package com.songyu.domains.auth.valueObject;

/**
 * <p>
 * 认证同步锁 key
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 11:07
 */
public class AuthLockKeys {

    /**
     * token 生成
     */
    public static final Object TOKEN_GENERATE = "T_G";

    /**
     * token 生成 jwk
     */
    public static final Object TOKEN_CLIENT_KEY_GENERATE = "T_C_K_G";

    /**
     * refresh token 生成 jwk
     */
    public static final Object REFRESH_TOKEN_CLIENT_KEY_GENERATE = "R_T_C_K_G";
}
