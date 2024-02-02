package com.songyu.commonutils;

import lombok.Getter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Base64;

/**
 * <p>
 * 通用 AES 加密工具类
 * </p>
 *
 * @author songYu
 * @since 2023/12/6 22:56
 */
public class CommonAESEncryptUtils {

    /**
     * 加密模式
     */
    public enum Mode {
        ECB
    }

    /**
     * 填充方式
     */
    public enum Padding {
        PKCS7Padding
    }

    /**
     * 加解密模式
     */
    @Getter
    public enum CryptMode {
        /**
         * 加密
         */
        ENCRYPT(Cipher.ENCRYPT_MODE),

        /**
         * 解密
         */
        DECRYPT(Cipher.DECRYPT_MODE),
        ;

        CryptMode(int value) {
            this.value = value;
        }

        private final int value;

    }

    public static byte[] crypto(byte[] bytes, byte[] key, CryptMode mode) {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            cipher.init(mode.getValue(), secretKeySpec);
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            throw new RuntimeException("AES 加解密失败", e);
        }
    }

    public static byte[] encryptBytes(byte[] bytes, byte[] keyBytes) {
        return crypto(bytes, keyBytes, CryptMode.ENCRYPT);
    }

    public static byte[] decryptBytes(byte[] bytes, byte[] keyBytes) {
        return crypto(bytes, keyBytes, CryptMode.DECRYPT);
    }

    public static String encryptString(String payload, String key) {
        byte[] bytes = CommonByteArrayUtils.parseBytes(32, new Object[]{key});
        return Base64.getEncoder().encodeToString(encryptBytes(payload.getBytes(StandardCharsets.UTF_8), bytes));
    }

    public static String decryptString(String payload, String key) {
        byte[] bytes = CommonByteArrayUtils.parseBytes(32, new Object[]{key});
        return new String(decryptBytes(Base64.getDecoder().decode(payload), bytes), StandardCharsets.UTF_8);
    }

    public static String encryptString(String payload) {
        return encryptString(payload, "阿克苏带来烦恼是觉得你发去问你；阿里飒飒的你父亲如果去");
    }

    public static String decryptString(String payload) {
        return decryptString(payload, "阿克苏带来烦恼是觉得你发去问你；阿里飒飒的你父亲如果去");
    }

    public static String encryptObject(Object payload, Object key) {
        byte[] bytes = CommonByteArrayUtils.parseBytes(32, new Object[]{key});
        return Base64.getEncoder().encodeToString(encryptBytes(CommonByteArrayUtils.objectToByteArrayJavaS(payload), bytes));
    }

    public static <T> T decryptObject(String payload, String key, Class<T> clazz) {
        byte[] bytes = CommonByteArrayUtils.parseBytes(32, new Object[]{key});
        return CommonByteArrayUtils.byteArrayToObjectJavaS(decryptBytes(Base64.getDecoder().decode(payload), bytes), clazz);
    }

    public static String encryptObject(Object payload) {
        return encryptObject(payload, "阿克苏带来烦恼是觉得你发wqertyuil父亲如果去");
    }

    public static <T> T decryptObject(String payload, Class<T> clazz) {
        return decryptObject(payload, "阿克苏带来烦恼是觉得你发wqertyuil父亲如果去", clazz);
    }

}
