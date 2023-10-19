package com.songyu.components.jsonweb;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.lang.JoseException;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * <p>
 * json web encrypt 相关业务
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 9:45
 */
public class JsonWebEncryptionService {

    public static String decrypt(String secret, PrivateKey privateKey) {
        JsonWebEncryption receiverJwe = new JsonWebEncryption();
        AlgorithmConstraints algConstraints = new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT,
                KeyManagementAlgorithmIdentifiers.RSA_OAEP);
        receiverJwe.setAlgorithmConstraints(algConstraints);
        AlgorithmConstraints encConstraints = new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT,
                ContentEncryptionAlgorithmIdentifiers.AES_256_GCM);
        receiverJwe.setContentEncryptionAlgorithmConstraints(encConstraints);
        receiverJwe.setKey(privateKey);
        try {
            receiverJwe.setCompactSerialization(secret);
            return receiverJwe.getPlaintextString();
        } catch (JoseException e) {
            throw new RuntimeException("解密失败", e);
        }
    }

    public static String encrypt(String payload, PublicKey publicKey) {
        JsonWebEncryption senderJwe = new JsonWebEncryption();
        senderJwe.setPlaintext(payload);
        senderJwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.RSA1_5);
        senderJwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_GCM);
        senderJwe.setKey(publicKey);
        try {
            return senderJwe.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException("加密失败", e);
        }
    }

}
