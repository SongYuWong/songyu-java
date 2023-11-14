package com.songyu.components.springboot.stater;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

/**
 * <p>
 * 环境变量安全工具
 * </p>
 *
 * @author songYu
 * @since 2023/11/3 18:18
 */
public class EnvSecureUtil {

    public static String encrypt(String plaintext, String password) {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentStringPBEConfig environmentStringPBEConfig = new EnvironmentStringPBEConfig();
        environmentStringPBEConfig.setAlgorithm("PBEWithMD5AndDES");
        environmentStringPBEConfig.setPassword(password);
        standardPBEStringEncryptor.setConfig(environmentStringPBEConfig);
        return standardPBEStringEncryptor.encrypt(plaintext);
    }

    public static String decrypt(String ciphertext, String password) {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentStringPBEConfig environmentStringPBEConfig = new EnvironmentStringPBEConfig();
        environmentStringPBEConfig.setAlgorithm("PBEWithMD5AndDES");
        environmentStringPBEConfig.setPassword(password);
        standardPBEStringEncryptor.setConfig(environmentStringPBEConfig);
        return standardPBEStringEncryptor.decrypt(ciphertext);
    }

}
