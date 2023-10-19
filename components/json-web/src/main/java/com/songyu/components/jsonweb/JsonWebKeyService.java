package com.songyu.components.jsonweb;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;

/**
 * <p>
 * json web key 相关依赖
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 18:24
 */
public class JsonWebKeyService {

    /**
     * 生成 RSA Json Web Key
     * @param keyId keyid
     * @return RSA Json Web Key
     */
    public static RsaJsonWebKey generateRsaJsonWebKey(String keyId) {
        RsaJsonWebKey rsaJsonWebKey;
        try {
            rsaJsonWebKey = RsaJwkGenerator.generateJwk(4096);
        } catch (JoseException e) {
            throw new RuntimeException("生成 JWK 失败", e);
        }
        rsaJsonWebKey.setKeyId(keyId);
        return rsaJsonWebKey;
    }

}
