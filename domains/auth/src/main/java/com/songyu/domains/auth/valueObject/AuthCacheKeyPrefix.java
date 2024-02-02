package com.songyu.domains.auth.valueObject;

/**
 * <p>
 * 认证缓存 key 前缀
 * </p>
 *
 * @author songYu
 * @since 2023/9/25 8:27
 */
public class AuthCacheKeyPrefix {

    /**
     * 激活邮箱
     */
    public static final String ACTIVATION_EMAIL = "S-A-U-A-C-F-";

    /**
     * 登录验证码
     */
    public static final String LOGIN_CAPTCHA = "S-A-U-L-C-F-";

    /**
     * 认证令牌
     */
    public static final String AUTH_TOKEN = "S-A-U-A-T-F-";

    /**
     * 认证刷新令牌
     */
    public static final String AUTH_REFRESH_TOKEN = "S-A-U-A-R-T-F-";

}
