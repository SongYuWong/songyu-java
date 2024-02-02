package com.songyu.components.jarlibcrypto.core;


import com.songyu.components.jsonweb.JsonWebEncryptionService;
import com.songyu.components.jsonweb.JsonWebKeyService;
import com.songyu.components.jsonweb.JsonWebSignatureService;
import com.songyu.components.jsonweb.JsonWebTokenService;
import org.jose4j.jwk.RsaJsonWebKey;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 令牌工具
 * </p>
 *
 * @author songYu
 * @since 2023/9/8 18:27
 */
public class JoseUtils {


    /**
     * 生成 JWK
     *
     * @return JWK
     */
    public static RsaJsonWebKey generateJWK(String keyId) {
        return JsonWebKeyService.generateRsaJsonWebKey(keyId);
    }

    public static String decryptJWE(String jwe, RsaJsonWebKey rsaJsonWebKey) {
        return JsonWebEncryptionService.decrypt(jwe, rsaJsonWebKey.getRsaPrivateKey());
    }

    public static String encryptJWE(String plaintext, RsaJsonWebKey rsaJsonWebKey) {
        return JsonWebEncryptionService.encrypt(plaintext, rsaJsonWebKey.getRsaPublicKey());
    }

    public static String signJWS(String plaintext, RsaJsonWebKey rsaJsonWebKey) {
        return JsonWebSignatureService.sign(plaintext, rsaJsonWebKey.getPrivateKey());
    }

    public static String verifyJWS(String JWS, RsaJsonWebKey rsaJsonWebKey) {
        return JsonWebSignatureService.verifySignature(JWS, rsaJsonWebKey.getRsaPublicKey());
    }

    public static String generateToken(RsaJsonWebKey jsonWebKey,
                                       String toWhom,
                                       Long expireTime,
                                       Long issuerTime,
                                       Map<String, Object> payload) {
        return JsonWebTokenService.generateToken(payload,
                "songyuwong@163.com",
                toWhom,
                "jar security",
                issuerTime,
                TimeUnit.MILLISECONDS,
                expireTime,
                TimeUnit.MILLISECONDS,
                jsonWebKey);
    }

}
