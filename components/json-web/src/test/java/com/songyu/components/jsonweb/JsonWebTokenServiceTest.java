package com.songyu.components.jsonweb;

import com.songyu.components.jsonweb.exception.*;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.ErrorCodes;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.junit.jupiter.api.Test;

import javax.xml.bind.DatatypeConverter;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

class JsonWebTokenServiceTest {

    @Test
    void parseToken() throws InvalidSubjectException, InvalidJwtException, ExpiredException, InvalidSignatureException, InvalidIssuerException, InvalidAudienceException {
        String token = "eyJraWQiOiJrMSIsImFsZyI6IlJTMjU2In0.eyJwYXlsb2FkIjoiUGF5bG9hZERhdGEiLCJpc3MiOiJJc3N1ZXIiLCJhdWQiOiJBdWRpZW5jZSIsImV4cCI6MTcwMzE1MDkzOCwiaWF0IjoxNzAzMTQ5MTM4LCJzdWIiOiJTdWJqZWN0In0.YRC92oczUortEL5k4hQxt3k74Dos_Y9g3slcK33ohuMuyO4gXzqiqfGdC2R3KtbJnyAyLUntXYe9aDyLC3JHyw9YWcLMt8BUGF6pgrDgA7Dnwvky8O7-CIv7TbdMpnJw-lP1G178dys6Yjg1R1ggj9Zvh1AfNjx5reiM3bLnx1auL0gpMZ7V9BnI8huzE2p-P6IxzkEhfcIIE5Yg2He5za3uO0zQoKU6ei3hV9431hIKVe7kEy89mBcY6NoV8JG0s8t_EfNpgAf0fXAPOGmyBvfOHJJ7d8_LCjWMz0SMq1I6H9Pog7jdpuaPdPJWAHayqcwtTrWCf1AU6qhIHyF55A";
        String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApO2S+Hue/UIbeh89Ecdk7t/KswRrvSiWq2mRkHSmj2Aaa0XfPsr4LyagGEYozGJyhZXRE7rKmro3Y0lkTJi7D9o0CJjDjYpYSmSYRl+PlF1kRLruh4NdQjULHK3eyNyNKA660mBmHLBqn1p5TRHRuSB2/OxhR6g6I8KUlLR1R+4u6X2oWIzMsuERGugTLPfXYVBfxhMGb6QJS6R/RZsNCxS6XXA7NFhwEj/n+IZV1qltQ2j5Fse85fBudlc0SysZMh/+t4DRREQypIbYI8wm8tVwTJOWZ1eAPucrI9wdxjB6LvZEHdhbKLwBOlAHVpig7KV6D2okedjWj/db7dvThwIDAQAB";
        PublicKey publicKey;
        KeyFactory rsa;
        try {
            rsa = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("根据 PEM 格式密钥生成公钥对象失败", e);
        }
        byte[] dePem = DatatypeConverter.parseBase64Binary(key);
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(dePem);
            publicKey = rsa.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("根据 PEM 格式密钥生成公钥对象失败", e);
        }
        JwtConsumerBuilder jwtConsumerBuilder = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(10)
                .setRequireSubject()
                .setExpectedSubject("Subject")
                .setExpectedIssuer("Issuer")
                .setExpectedAudience("Audience");
        jwtConsumerBuilder.setVerificationKey(publicKey);
        JwtConsumer jwtConsumer = jwtConsumerBuilder
                .setJwsAlgorithmConstraints(
                        AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256)
                .build();
        try {
            JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
            try {
                String payload = jwtClaims.getClaimValue("payload", String.class);
                System.out.println(payload);
            } catch (MalformedClaimException e) {
                throw new RuntimeException(e);
            }
        } catch (InvalidJwtException e) {
            if (e.hasErrorCode(ErrorCodes.EXPIRED)) {
                throw new ExpiredException("token 已过期", e);
            }
            if (e.hasErrorCode(ErrorCodes.SUBJECT_INVALID)) {
                throw new InvalidSubjectException("无效的 token 主题", e);
            }
            if (e.hasErrorCode(ErrorCodes.ISSUER_INVALID)) {
                throw new InvalidIssuerException("无效的 token 构建者", e);
            }
            if (e.hasErrorCode(ErrorCodes.AUDIENCE_INVALID)) {
                throw new InvalidAudienceException("无效的 token 使用者", e);
            }
            if (e.hasErrorCode(ErrorCodes.SIGNATURE_INVALID)) {
                throw new InvalidSignatureException("无效的 token 被篡改", e);
            }
            throw e;
        }
    }
}