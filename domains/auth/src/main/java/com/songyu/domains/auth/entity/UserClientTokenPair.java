package com.songyu.domains.auth.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户客户端令牌对
 * </p>
 *
 * @author songYu
 * @since 2023/9/25 15:51
 */
@Getter
@Setter
public class UserClientTokenPair {

    /**
     * 认证令牌
     */
    private String token;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 客户端唯一号
     */
    private String clientId;

    public UserClientTokenPair(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
