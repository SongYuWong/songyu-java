package com.songyu.components.springboot.data;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.keygen.KeyGeneratorFactory;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * mybatis plus flex 启动配置类
 * </p>
 *
 * @author songYu
 * @since 2023/9/18 10:17
 */
@Configuration
@MapperScan("com.songyu.domains.infrastructure.mapper")
public class MyBatisFlexConfiguration implements MyBatisFlexCustomizer {

    @Override
    public void customize(FlexGlobalConfig flexGlobalConfig) {
        KeyGeneratorFactory.register("nanoid", new NanoIDKeyGenerator());
        flexGlobalConfig.setPrintBanner(false);
    }

}
