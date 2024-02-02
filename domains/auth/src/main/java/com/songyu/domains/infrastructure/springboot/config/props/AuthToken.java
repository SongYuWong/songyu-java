package com.songyu.domains.infrastructure.springboot.config.props;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 认证令牌相关配置属性
 * </p>
 *
 * @author songYu
 * @since 2023/9/25 15:24
 */
@Getter
@Setter
public class AuthToken {

    /**
     * 认证令牌生效时间（单位秒）
     */
    private Long issueTimeSeconds = 0L;

    /**
     * 认证令牌过期时间（单位秒）
     */
    private Long expireTimeSeconds = 300L;

    public AuthToken(Long issueTimeSeconds, Long expireTimeSeconds) {
        this.issueTimeSeconds = issueTimeSeconds;
        this.expireTimeSeconds = expireTimeSeconds;
    }

    public AuthToken() {
    }

}
