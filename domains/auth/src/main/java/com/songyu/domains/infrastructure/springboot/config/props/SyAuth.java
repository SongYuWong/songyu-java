package com.songyu.domains.infrastructure.springboot.config.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * <p>
 * 认证相关配置
 * </p>
 *
 * @author songYu
 * @since 2023/9/25 15:20
 */
@Getter
@Setter
@ConfigurationProperties("sy.auth")
public class SyAuth {

    /**
     * 认证令牌
     */
    @NestedConfigurationProperty
    private AuthToken token = new AuthToken(0L, 300L);

    /**
     * 认证刷新令牌
     */
    @NestedConfigurationProperty
    private AuthToken refreshToken = new AuthToken(0L, 600L);

}
