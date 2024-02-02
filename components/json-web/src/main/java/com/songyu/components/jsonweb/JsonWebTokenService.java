package com.songyu.components.jsonweb;

import cn.hutool.json.JSONUtil;
import com.songyu.components.jsonweb.exception.*;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.ErrorCodes;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * json web token 相关业务
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 9:42
 */
public class JsonWebTokenService {

    /**
     * 生成认证令牌
     *
     * @param payload         令牌额外载荷数据（密码之类敏感信息不推荐）
     * @param issuer          令牌提供者约定
     * @param audience        令牌使用者约定
     * @param subject         令牌主题约定
     * @param issuerAt        令牌颁发有效开始时间
     * @param issuerTimeUnit  时间单位
     * @param expiredAt       令牌使用过期时间
     * @param expiredTimeUnit 时间单位
     * @param jsonWebKey      RSA加密密钥
     * @return token 令牌
     */
    public static String generateToken(Object payload,
                                       String issuer,
                                       String audience,
                                       String subject,
                                       long issuerAt,
                                       TimeUnit issuerTimeUnit,
                                       long expiredAt,
                                       TimeUnit expiredTimeUnit,
                                       RsaJsonWebKey jsonWebKey) {
        JwtClaims claims = new JwtClaims();
        claims.setClaim("payload", JSONUtil.toJsonStr(payload));
        claims.setIssuer(issuer);
        claims.setAudience(audience);
        claims.setExpirationTimeMinutesInTheFuture(issuerTimeUnit.toMillis(issuerAt));
        claims.setIssuedAt(NumericDate.fromMilliseconds(expiredTimeUnit.toMillis(expiredAt)));
        claims.setSubject(subject);
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(jsonWebKey.getPrivateKey());
        jws.setKeyIdHeaderValue(jsonWebKey.getKeyId());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException("生成认证信息失败", e);
        }
    }

    /**
     * 解析认证令牌
     *
     * @param payloadType 额外载荷类型
     * @param token       认证令牌
     * @param issuer      令牌提供者约定
     * @param audience    令牌使用者约定
     * @param subject     令牌主题
     * @param jsonWebKey  RSA加密密钥
     * @param <T>         额外载荷泛型定义
     * @return 认证令牌中携带的额外载荷信息
     * @throws ExpiredException          令牌已过期异常
     * @throws InvalidSubjectException   非法令牌主题
     * @throws InvalidIssuerException    非法令牌提供者
     * @throws InvalidAudienceException  非法令牌使用者
     * @throws InvalidSignatureException 非法令牌已被篡改
     * @throws InvalidJwtException       无效令牌
     */
    public static <T> T parseToken(Class<T> payloadType,
                                   String token,
                                   String issuer,
                                   String audience,
                                   String subject,
                                   RsaJsonWebKey jsonWebKey)
            throws ExpiredException, InvalidSubjectException, InvalidIssuerException, InvalidAudienceException,
            InvalidSignatureException, InvalidJwtException {
        JwtConsumerBuilder jwtConsumerBuilder = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(10)
                .setRequireSubject()
                .setExpectedSubject(subject)
                .setExpectedIssuer(issuer)
                .setExpectedAudience(audience);
        if (jsonWebKey == null) {
            jwtConsumerBuilder.setSkipSignatureVerification();
        } else {
            jwtConsumerBuilder.setVerificationKey(jsonWebKey.getPublicKey());
        }
        JwtConsumer jwtConsumer = jwtConsumerBuilder
                .setJwsAlgorithmConstraints(
                        AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256)
                .build();
        try {
            JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
            try {
                String payload = jwtClaims.getClaimValue("payload", String.class);
                try {
                    return JSONUtil.toBean(payload, payloadType);
                } catch (Exception e) {
                    //noinspection unchecked
                    return (T) payload;
                }
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
