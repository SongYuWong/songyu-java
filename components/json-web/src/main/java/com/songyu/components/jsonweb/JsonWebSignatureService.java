package com.songyu.components.jsonweb;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * <p>
 * json web signature 相关业务
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 9:43
 */
public class JsonWebSignatureService {

    public static String verifySignature(String sign, PublicKey publicKey) {
        JsonWebSignature jws = getJsonWebSignature(sign, publicKey);
        boolean verifySignature;
        try {
            verifySignature = jws.verifySignature();
        } catch (JoseException e) {
            throw new RuntimeException("验证签名失败", e);
        }
        if (verifySignature) {
            try {
                return jws.getPayload();
            } catch (JoseException e) {
                throw new RuntimeException("获取签名数据失败", e);
            }
        } else {
            throw new RuntimeException("验签不通过");
        }
    }

    private static JsonWebSignature getJsonWebSignature(String sign, PublicKey publicKey) {
        JsonWebSignature jws = new JsonWebSignature();
        AlgorithmConstraints algorithmConstraints =
                new AlgorithmConstraints(
                        AlgorithmConstraints.ConstraintType.PERMIT,
                        AlgorithmIdentifiers.RSA_PSS_USING_SHA512
                );
        jws.setAlgorithmConstraints(algorithmConstraints);
        try {
            jws.setCompactSerialization(sign);
        } catch (JoseException e) {
            throw new RuntimeException("数据验签失败", e);
        }
        jws.setKey(publicKey);
        return jws;
    }

    public static String sign(String payload, PrivateKey privateKey) {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(payload);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_PSS_USING_SHA512);
        jws.setKey(privateKey);
        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException("数据加签失败", e);
        }
    }

}
