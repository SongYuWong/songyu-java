package com.songyu.components.api;

import com.songyu.commonutils.CommonRsaKeyPairUtils;
import com.songyu.components.jsonweb.JsonWebEncryptionService;
import com.songyu.components.jsonweb.JsonWebKeyService;
import com.songyu.components.jsonweb.JsonWebSignatureService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwk.RsaJsonWebKey;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * <p>
 * 数据安全工具类
 * </p>
 *
 * @author songYu
 * @since 2023/9/15 10:51
 */
@Slf4j
public class ApiSecureManager {

    /**
     * RSA 公钥
     */
    @Getter
    private final String publicKeyStr;

    /**
     * RSA 私钥
     */
    private final PrivateKey privateKey;

    /**
     * RSA 公钥
     */
    @Getter
    private final PublicKey publicKey;

    private ApiSecureManager(String keyId) {
        RsaJsonWebKey rsaJsonWebKey = JsonWebKeyService.generateRsaJsonWebKey(keyId);
        this.privateKey = rsaJsonWebKey.getPrivateKey();
        this.publicKey = rsaJsonWebKey.getPublicKey();
        this.publicKeyStr = CommonRsaKeyPairUtils.exportX509PublicKey(rsaJsonWebKey.getPublicKey());
    }

    /**
     * 接口数据验证签名
     *
     * @param data 解密的数据
     * @param key  输入端公钥
     * @return 解密后数据串
     */
    public String verifySecureDataStrSign(String data, String key) {
        try {
            // 验证签名
            return JsonWebSignatureService.verifySignature(data, CommonRsaKeyPairUtils.importX509PublicKey(key));
        } catch (Exception e) {
            throw new RuntimeException("数据安全不通过", e);
        }
    }


    /**
     * 接口数据解密
     *
     * @param data 解密的数据
     * @param key  输入端公钥
     * @return 解密后数据串
     */
    public String decryptSecureDataStrSigned(String data, String key) {
        try {
            // 验证签名
            String sec = JsonWebSignatureService.verifySignature(data, CommonRsaKeyPairUtils.importX509PublicKey(key));
            // 解密
            return JsonWebEncryptionService.decrypt(sec, this.privateKey);
        } catch (Exception e) {
            throw new RuntimeException("数据安全不通过", e);
        }
    }

    /**
     * 接口数据加密
     *
     * @param payload 被加密串
     * @param key     加密密钥
     * @return 加密后的数据
     */
    public String encryptSecurityDataStr(String payload, String key) {
        //接收端的公钥加密
        String sec = JsonWebEncryptionService.encrypt(payload, CommonRsaKeyPairUtils.importX509PublicKey(key));
        //私钥加签
        return JsonWebSignatureService.sign(sec, this.privateKey);
    }

    /**
     * 数据安全控制单例工厂
     *
     * @param key 数据安全控制唯一号
     * @return 数据安全控制对象
     */
    public static ApiSecureManager getSecurity(String key) {
        return SecurityKeyEnum.NORMAL.getById(key);
    }

    /**
     * 数据安全控制单例枚举
     */
    private enum SecurityKeyEnum {

        NORMAL(new ApiSecureManager("NORMAL"));

        private final ApiSecureManager apiSecureManager;

        SecurityKeyEnum(ApiSecureManager apiSecureManager) {
            this.apiSecureManager = apiSecureManager;
        }

        private ApiSecureManager getById(String keyId) {
            ApiSecureManager data = valueOf(keyId).apiSecureManager;
            if (data == null) {
                throw new RuntimeException("非法的 key id");
            } else {
                return data;
            }
        }

    }

}
