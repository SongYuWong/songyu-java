package com.songyu.components.jarlibcrypto.core;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import java.security.PrivateKey;
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
        RsaJsonWebKey rsaJsonWebKey;
        try {
            rsaJsonWebKey = RsaJwkGenerator.generateJwk(4096);
        } catch (JoseException e) {
            throw new RuntimeException("生成 JWK 失败", e);
        }
        rsaJsonWebKey.setKeyId(keyId);
        return rsaJsonWebKey;
    }

    /**
     * 解析令牌
     *
     * @param rsaJsonWebKey 令牌加签密钥
     * @param fromWhom      令牌提供商
     * @param toWhom        令牌使用商
     * @param jwt           令牌
     * @return 解析出的令牌中的信息
     */
    public static Map<String, Object> parseToken(RsaJsonWebKey rsaJsonWebKey,
                                                 String fromWhom,
                                                 String toWhom,
                                                 String jwt) {
        // Use JwtConsumerBuilder to construct an appropriate JwtConsumer, which will
        // be used to validate and process the JWT.
        // The specific validation requirements for a JWT are context dependent, however,
        // it is typically advisable to require a (reasonable) expiration time, a trusted issuer, and
        // an audience that identifies your system as the intended recipient.
        // If the JWT is encrypted too, you need only provide a decryption key or
        // decryption key resolver to the builder.
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setAllowedClockSkewInSeconds(10) // allow some leeway in validating time based claims to account for clock skew
                .setRequireSubject()
                .setExpectedSubject("jar security") // the JWT must have a subject claim
                .setExpectedIssuer(fromWhom) // whom the JWT needs to have been issued by
                .setExpectedAudience(toWhom) // to whom the JWT is intended for
                .setVerificationKey(rsaJsonWebKey.getKey()) // verify the signature with the public key
                .setJwsAlgorithmConstraints( // only allow the expected signature algorithm(s) in the given context
                        AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256) // which is only RS256 here
                .build(); // create the JwtConsumer instance
        try {
            //  Validate the JWT and process it to the Claims
            return jwtConsumer.processToClaims(jwt).getClaimsMap();
        } catch (InvalidJwtException e) {
            throw new RuntimeException("启动失败");
        }
    }

    public static String decryptJWE(String jwe, RsaJsonWebKey rsaJsonWebKey) {
        // That other party, the receiver, can then use JsonWebEncryption to decrypt the message.
        JsonWebEncryption receiverJwe = new JsonWebEncryption();
        // Set the algorithm constraints based on what is agreed upon or expected from the sender
        AlgorithmConstraints algConstraints = new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT, KeyManagementAlgorithmIdentifiers.RSA_OAEP);
        receiverJwe.setAlgorithmConstraints(algConstraints);
        AlgorithmConstraints encConstraints = new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT, ContentEncryptionAlgorithmIdentifiers.AES_256_GCM);
        receiverJwe.setContentEncryptionAlgorithmConstraints(encConstraints);
        // Symmetric encryption, like we are doing here, requires that both parties have the same key.
        // The key will have had to have been securely exchanged out-of-band somehow.
        receiverJwe.setKey(rsaJsonWebKey.getPublicKey());
        // Set the compact serialization on new Json Web Encryption object
        try {
            receiverJwe.setCompactSerialization(jwe);
            // Get the message that was encrypted in the JWE. This step performs the actual decryption steps.
            return receiverJwe.getPlaintextString();
        } catch (JoseException e) {
            throw new RuntimeException("解密失败", e);
        }
    }

    public static String encryptJWE(String plaintext, RsaJsonWebKey rsaJsonWebKey) {
        //
        // An example showing the use of JSON Web Encryption (JWE) to encrypt and then decrypt some content
        // using a symmetric key and direct encryption.
        // Create a new Json Web Encryption object
        JsonWebEncryption senderJwe = new JsonWebEncryption();
        // The plaintext of the JWE is the message that we want to encrypt.
        senderJwe.setPlaintext(plaintext);
        // Set the "alg" header, which indicates the key management mode for this JWE.
        // In this example we are using the direct key management mode, which means
        // the given key will be used directly as the content encryption key.
        senderJwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.RSA1_5);
        // Set the "enc" header, which indicates the content encryption algorithm to be used.
        // This example is using AES_128_CBC_HMAC_SHA_256 which is a composition of AES CBC
        // and HMAC SHA2 that provides authenticated encryption.
        senderJwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_GCM);
        // Set the key on the JWE. In this case, using direct mode, the key will used directly as
        // the content encryption key. AES_128_CBC_HMAC_SHA_256, which is being used to encrypt the
        // content requires a 256 bit key.
        senderJwe.setKey(rsaJsonWebKey.getPrivateKey());
        // Produce the JWE compact serialization, which is where the actual encryption is done.
        // The JWE compact serialization consists of five base64url encoded parts
        // combined with a dot ('.') character in the general format of
        // <header>.<encrypted key>.<initialization vector>.<ciphertext>.<authentication tag>
        // Direct encryption doesn't use an encrypted key so that field will be an empty string
        // in this case.
        try {
            return senderJwe.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    public static String signJWS(String plaintext, RsaJsonWebKey rsaJsonWebKey) {   //
        // An example of signing using JSON Web Signature (JWS)
        // Create a new JsonWebSignature
        JsonWebSignature jws = new JsonWebSignature();
        // Set the payload, or signed content, on the JWS object
        jws.setPayload(plaintext);
        // Set the signature algorithm on the JWS that will integrity protect the payload
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_PSS_USING_SHA256);
        // Set the signing key on the JWS
        // Note that your application will need to determine where/how to get the key
        // and here we just use an example from the JWS spec
        jws.setKey(rsaJsonWebKey.getPrivateKey());
        // Sign the JWS and produce the compact serialization or complete JWS representation, which
        // is a string consisting of three dot ('.') separated base64url-encoded
        // parts in the form Header.Payload.Signature
        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException("数据加签失败", e);
        }
    }

    public static String verifyJWS(String JWS, RsaJsonWebKey rsaJsonWebKey) {
        //
        // An example of signature verification using JSON Web Signature (JWS)
        // Create a new JsonWebSignature
        JsonWebSignature jws = new JsonWebSignature();
        // Set the algorithm constraints based on what is agreed upon or expected from the sender
        jws.setAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_PSS_USING_SHA256));
        // Set the compact serialization on the JWS
        try {
            jws.setCompactSerialization(JWS);
        } catch (JoseException e) {
            throw new RuntimeException("数据验签失败", e);
        }
        jws.setKey(rsaJsonWebKey.getPublicKey());
        // Check the signature
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

    public static String generateToken(PrivateKey privateKey,
                                       String keyId,
                                       String toWhom,
                                       Long expireTime,
                                       Long issuerTime,
                                       Map<String, Object> payload) {
        JwtClaims claims = new JwtClaims();
        if (payload != null && payload.size() > 0) {
            payload.forEach(claims::setClaim);
        }
        claims.setIssuer("songyuwong@163.com");
        claims.setAudience(toWhom);
        claims.setExpirationTimeMinutesInTheFuture(TimeUnit.MILLISECONDS.toMinutes(expireTime));
        claims.setIssuedAt(NumericDate.fromMilliseconds(TimeUnit.MILLISECONDS.toMillis(issuerTime)));
        claims.setSubject("jar security");
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(privateKey);
        jws.setKeyIdHeaderValue(keyId);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }
    }
}
