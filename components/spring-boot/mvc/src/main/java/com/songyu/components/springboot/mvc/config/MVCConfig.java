package com.songyu.components.springboot.mvc.config;

import com.songyu.components.api.ApiSecureManager;
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
@Configuration
@ComponentScan("com.songyu.components.springboot.mvc")
public class MVCConfig {

    @Bean
    public ApiSecureManager secureData() {
        return ApiSecureManager.getSecurity("NORMAL");
    }

}
