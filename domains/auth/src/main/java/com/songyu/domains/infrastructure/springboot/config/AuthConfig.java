package com.songyu.domains.infrastructure.springboot.config;

import com.songyu.components.cache.CacheService;
import com.songyu.components.cache.LocalCacheService;
import com.songyu.components.lock.LockService;
import com.songyu.components.lock.local.LocalLockService;
import com.songyu.domains.infrastructure.springboot.config.props.SyAuth;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 认证相关配置
 * </p>
 *
 * @author songYu
 * @since 2023/9/25 15:21
 */
@Configuration
@EnableConfigurationProperties(SyAuth.class)
public class AuthConfig {

    @Bean
    public LockService<?> lockService() {
        return new LocalLockService();
    }

    @Bean
    public CacheService cacheService() {
        return new LocalCacheService();
    }

}
