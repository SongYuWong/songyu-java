package com.songyu.components.springboot.mvc.config;

import com.songyu.components.api.ApiSecureManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * MVC 启动配置类
 * </p>
 *
 * @author songYu
 * @since 2023/9/20 14:35
 */
@Slf4j
@Configuration
@ComponentScan("com.songyu.components.springboot.mvc")
public class MVCConfig {

    @Bean
    public ApiSecureManager apiSecureManager() {
        return ApiSecureManager.getSecurity("NORMAL");
    }

}
