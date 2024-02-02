package com.songyu.components.springboot.stater;

import com.songyu.commonutils.CommonConsoleUIUtils;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
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

    @Bean(name = "encryptablePropertyResolver")
    EncryptablePropertyResolver encryptablePropertyResolver() {
        class MyEncryptPropertyResolver implements EncryptablePropertyResolver {
            private final PooledPBEStringEncryptor encryptor;

            public MyEncryptPropertyResolver(String password) {
                this.encryptor = new PooledPBEStringEncryptor();
                SimpleStringPBEConfig config = new SimpleStringPBEConfig();
                config.setPasswordCharArray(password.toCharArray());
                config.setAlgorithm("PBEWithMD5AndDES");
                config.setPoolSize(1);
                config.setStringOutputType("base64");
                encryptor.setConfig(config);
            }

            @Override
            public String resolvePropertyValue(String value) {
                if (value != null && value.startsWith("env.key>>")) {
                    return encryptor.decrypt(value.substring("env.key>>".length()));
                }
                return value;
            }
        }
//        String next = CommonConsoleUIUtils.hiddenScanner("请输入配置信息密钥：");
        return new MyEncryptPropertyResolver("KMKCJLYIFKKIQUBU");
    }

}
