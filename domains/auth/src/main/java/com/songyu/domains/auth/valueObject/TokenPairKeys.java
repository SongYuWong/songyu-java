package com.songyu.domains.auth.valueObject;

/**
 * <p>
 * 令牌有效数据 key
 * </p>
 *
 * @author songYu
 * @since 2023/9/25 15:07
 */
public class TokenPairKeys {

    /**
     * 资源标签集合 key
     */
    public static final String RESOURCES = "res";

    /**
     * 令牌数据 key
     */
    public static final String TOKEN = "token";

    /**
     * 刷新令牌主题
     */
    public static final String SUBJECT_REFRESH_TOKEN = "CLIENT-REFRESH-TOKEN";

    /**
     * 刷新令牌发行商
     */
    public static final String ISSUER_REFRESH_TOKEN = "SY-AUTH-REFRESH";

    /**
     * 令牌发行商
     */
    public static final String ISSUER_TOKEN = "SY-AUTH";

    /**
     * 令牌主题
     */
    public static final String SUBJECT_TOKEN = "CLIENT-TOKEN";
}
