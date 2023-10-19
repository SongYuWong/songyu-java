package com.songyu.components.jsonweb;

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
        claims.setClaim("payload", payload);
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

    public static <T> T parseToken(Class<T> payloadType,
                                   String token,
                                   String issuer,
                                   String audience,
                                   String subject,
                                   RsaJsonWebKey jsonWebKey)
            throws ExpiredException, InvalidSubjectException, InvalidIssuerException, InvalidAudienceException,
            InvalidSignatureException, InvalidJwtException {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(10)
                .setRequireSubject()
                .setExpectedSubject(subject)
                .setExpectedIssuer(issuer)
                .setExpectedAudience(audience)
                .setVerificationKey(jsonWebKey.getPublicKey())
                .setJwsAlgorithmConstraints(
                        AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256)
                .build();
        try {
            JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
            try {
                return jwtClaims.getClaimValue("payload", payloadType);
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
