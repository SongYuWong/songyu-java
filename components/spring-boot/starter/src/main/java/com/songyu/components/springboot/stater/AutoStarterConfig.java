package com.songyu.components.springboot.stater;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 自动装配启动配置类
 * </p>
 *
 * @author songYu
 * @since 2023/9/20 11:52
 */
@Configuration
@ComponentScan("com.songyu.domains.infrastructure.springboot")
public class AutoStarterConfig {

}
