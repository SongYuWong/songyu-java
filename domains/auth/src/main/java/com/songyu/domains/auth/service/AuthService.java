package com.songyu.domains.auth.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.songyu.commonutils.CommonStringUtils;
import com.songyu.components.cache.CacheService;
import com.songyu.components.captcha.Captcha;
import com.songyu.components.captcha.CaptchaFactory;
import com.songyu.components.captcha.CaptchaType;
import com.songyu.components.captcha.clickimagetext.ClickImageTextPointsVerify;
import com.songyu.components.jsonweb.JsonWebKeyService;
import com.songyu.components.jsonweb.JsonWebTokenService;
import com.songyu.components.jsonweb.exception.*;
import com.songyu.components.lock.LockService;
import com.songyu.components.springboot.email.EmailConfig;
import com.songyu.components.springboot.email.EmailSendService;
import com.songyu.domains.auth.aggregate.UserLogin;
import com.songyu.domains.auth.aggregate.UserRegistered;
import com.songyu.domains.auth.entity.User;
import com.songyu.domains.auth.entity.UserClient;
import com.songyu.domains.auth.entity.UserClientTokenPair;
import com.songyu.domains.auth.exception.IllegalUserInfoException;
import com.songyu.domains.auth.exception.IllegalUserStatusException;
import com.songyu.domains.auth.exception.UserNotUniqueException;
import com.songyu.domains.auth.valueObject.*;
import com.songyu.domains.infrastructure.springboot.config.props.SyAuth;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.consumer.InvalidJwtException;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户管理服务
 * </p>
 *
 * @author songYu
 * @since 2023/9/20 10:53
 */
public abstract class AuthService {

    private final UserManagerService userManagerService;

    private final EmailSendService emailSendService;

    private final CacheService cacheService;

    private final ResourceManagerService resourceManagerService;

    private final SyAuth syAuth;

    private final LockService<?> lockService;

    public AuthService(UserManagerService userManagerService,
                       EmailSendService emailSendService,
                       CacheService cacheService,
                       ResourceManagerService resourceManagerService,
                       SyAuth syAuth,
                       LockService<?> lockService) {
        this.userManagerService = userManagerService;
        this.emailSendService = emailSendService;
        this.cacheService = cacheService;
        this.resourceManagerService = resourceManagerService;
        this.syAuth = syAuth;
        this.lockService = lockService;
    }

    /**
     * 注册
     *
     * @param user 用户信息
     */
    public void signup(User user) {
        try {
            userManagerService.createNewUser(user);
        } catch (IllegalUserInfoException | UserNotUniqueException e) {
            throw new RuntimeException(e);
        }
        sendUserActiveCode(user);
    }

    /**
     * 获取用户激活码 redis 缓存 key
     *
     * @param user 用户信息
     * @return redis 缓存 key
     */
    private static String getUserActiveCodeRedisKey(User user) {
        if (CommonStringUtils.isBlank(user.getUserEmail())) {
            throw new RuntimeException("激活码对应用户邮箱不能为空。");
        }
        return AuthCacheKeyPrefix.ACTIVATION_EMAIL.concat(String.valueOf(user.getUserEmail().hashCode()));
    }

    /**
     * 发送用户激活码
     *
     * @param user 用户信息
     */
    private void sendUserActiveCode(User user) {
        String redisKey = getUserActiveCodeRedisKey(user);
        String activeCode = cacheService.get(redisKey, String.class);
        if (CommonStringUtils.isNotBlank(activeCode)) {
            emailSendService.sendHtml(EmailConfig.EmailSender, user.getUserEmail(), "用户", activeCode);
        } else {
            activeCode = NanoIdUtils.randomNanoId().toUpperCase();
            cacheService.set(redisKey, activeCode, 5, TimeUnit.MINUTES);
            emailSendService.sendHtml(EmailConfig.EmailSender, user.getUserEmail(), "用户", activeCode);
        }
    }

    /**
     * 激活用户
     *
     * @param userRegistered 用户激活信息
     */
    public void activationUser(UserRegistered userRegistered) {
        String redisKey = getUserActiveCodeRedisKey(userRegistered.getUser());
        String activeCode = cacheService.get(redisKey, String.class);
        if (!userRegistered.ifActiveCodeValid(activeCode)) {
            throw new RuntimeException("无效的激活码！");
        } else {
            userManagerService.activateUser(userRegistered.getUser());
        }
    }

    /**
     * 生成登录验证码
     *
     * @param userLogin 用户登录信息
     * @return 生成的验证码
     */
    public Captcha generateCaptcha(UserLogin userLogin) {
        String clientId = userLogin.getClientId();
        if (CommonStringUtils.isBlank(clientId)) {
            throw new RuntimeException("缺少必要的客户端唯一号");
        } else {
            Captcha captcha = CaptchaFactory.generatorCaptcha(CaptchaType.CLICK_IMAGE_TEXT, clientId);
            cacheService.set(getCaptchaRedisKey(clientId), captcha.getVerifyInfo(), 5, TimeUnit.MINUTES);
            return captcha.clearVerifyInfo();
        }
    }

    /**
     * 获取验证码 redis 缓存 key
     *
     * @param clientId 客户端唯一号
     * @return 验证码 redis 缓存 key
     */
    private static String getCaptchaRedisKey(String clientId) {
        if (clientId == null) {
            throw new RuntimeException("缺少客户端唯一号");
        }
        return AuthCacheKeyPrefix.LOGIN_CAPTCHA.concat(String.valueOf(clientId.hashCode()));
    }

    /**
     * 登录
     *
     * @param userLogin 用户登录信息
     * @return 用户客户端资源聚合
     */
    public UserClientTokenPair login(UserLogin userLogin) {
        // 校验验证码
        checkCaptcha(userLogin);
        // 校验用户信息
        User user = checkLoginUserInfo(userLogin.getUser());
        // 生成用户认证信息
        Set<String> resourceLabels = resourceManagerService.getUserResourceLabels(user.getUserCode());
        String clientToken = generateClientToken(resourceLabels, userLogin.getClientId());
        String clientRefreshToken = generateClientRefreshToken(clientToken, userLogin.getClientId());
        UserClientTokenPair userClientTokenPair = new UserClientTokenPair(clientToken, clientRefreshToken);
        cacheService.set(getAuthTokenCacheKey(userLogin.getClientId()),
                clientToken,
                syAuth.getToken().getExpireTimeSeconds(),
                TimeUnit.SECONDS);
        cacheService.set(getAuthRefreshTokenCacheKey(userLogin.getClientId()),
                clientToken,
                syAuth.getToken().getExpireTimeSeconds(),
                TimeUnit.SECONDS);
        // 记录用户登录客户端
        UserClient userClient = new UserClient();
        userClient.setUserClientCode(userLogin.getClientId());
        userClient.setUserClientKey(userLogin.getClientKey());
        userClient.setUserClientFingerprint(userLogin.getClientFingerprint());
        userClient.setUserCode(user.getUserCode());
        userManagerService.addNewUserClient(userClient);
        return userClientTokenPair;
    }

    /**
     * 获取认证刷新令牌缓存 key
     *
     * @param clientId 认证客户端唯一号
     * @return 认证刷新令牌缓存key
     */
    private String getAuthRefreshTokenCacheKey(String clientId) {
        return AuthCacheKeyPrefix.AUTH_REFRESH_TOKEN.concat(clientId);
    }

    /**
     * 获取认证令牌缓存 key
     *
     * @param clientId 认证客户端唯一号
     * @return 认证令牌缓存key
     */
    private String getAuthTokenCacheKey(String clientId) {
        return AuthCacheKeyPrefix.AUTH_TOKEN.concat(clientId);
    }

    /**
     * 生成用户客户端认证令牌
     *
     * @param resourceLabels 用户资源标签集合
     * @param clientId       客户端 id
     * @return 认证令牌
     */
    private String generateClientToken(
            Set<String> resourceLabels,
            String clientId
    ) {
        RsaJsonWebKey rsaJsonWebKey = getOrDefaultClientTokenJWK();
        return JsonWebTokenService.generateToken(resourceLabels, TokenPairKeys.ISSUER_TOKEN, clientId, TokenPairKeys.SUBJECT_TOKEN,
                syAuth.getToken().getIssueTimeSeconds(),
                TimeUnit.SECONDS,
                syAuth.getToken().getExpireTimeSeconds(),
                TimeUnit.SECONDS,
                rsaJsonWebKey);
    }

    /**
     * 获取或者生成 生成客户端 token 的 rsa jwk
     *
     * @return rsa jwk
     */
    private RsaJsonWebKey getOrDefaultClientTokenJWK() {
        final RsaJsonWebKey[] rsaJsonWebKey = new RsaJsonWebKey[1];
        while (true) {
            try {
                if (lockService.runWithLock(AuthLockKeys.TOKEN_CLIENT_KEY_GENERATE, () -> {
                    rsaJsonWebKey[0] = cacheService.get(AuthCacheKey.TOKEN_RSA_JSON_KEY, RsaJsonWebKey.class);
                    if (rsaJsonWebKey[0] == null) {
                        rsaJsonWebKey[0] = JsonWebKeyService.generateRsaJsonWebKey(RsaJsonWebKeyIds.TOKEN_CLIENT_GENERATE);
                    }
                    cacheService.set(AuthCacheKey.TOKEN_RSA_JSON_KEY, rsaJsonWebKey[0]);
                }, 10, TimeUnit.SECONDS)) {
                    break;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("获取 token 生成密钥失败", e);
            }
        }
        return rsaJsonWebKey[0];
    }

    /**
     * 解析用户客户端认证令牌
     *
     * @param token    认证令牌
     * @param clientId 客户端唯一号
     * @return 用户资源标签集合
     * @throws InvalidSubjectException   非法的主题
     * @throws InvalidJwtException       非法的令牌
     * @throws ExpiredException          令牌已过期
     * @throws InvalidSignatureException 非法的签名
     * @throws InvalidIssuerException    非法的 提供商
     * @throws InvalidAudienceException  非法的使用者
     */
    private Set<String> parseClientToken(String token, String clientId) throws InvalidSubjectException, InvalidJwtException,
            ExpiredException, InvalidSignatureException, InvalidIssuerException, InvalidAudienceException {
        RsaJsonWebKey rsaJsonWebKey = getOrDefaultClientTokenJWK();
        //noinspection unchecked
        return JsonWebTokenService.parseToken(Set.class,
                token,
                TokenPairKeys.ISSUER_TOKEN,
                clientId,
                TokenPairKeys.SUBJECT_TOKEN,
                rsaJsonWebKey);
    }

    /**
     * 生成客户端认证刷新令牌
     *
     * @param token    认证令牌
     * @param clientId 客户端 id
     * @return 认证刷新令牌
     */
    private String generateClientRefreshToken(
            String token,
            String clientId
    ) {
        RsaJsonWebKey rsaJsonWebKey = getOrDefaultClientRefreshTokenJWK();
        return JsonWebTokenService.generateToken(token, TokenPairKeys.ISSUER_REFRESH_TOKEN, clientId, TokenPairKeys.SUBJECT_REFRESH_TOKEN,
                syAuth.getRefreshToken().getIssueTimeSeconds(),
                TimeUnit.SECONDS,
                syAuth.getRefreshToken().getExpireTimeSeconds(),
                TimeUnit.SECONDS,
                rsaJsonWebKey);
    }

    /**
     * 获取或者生成 生成客户端 refresh token 的 rsa jwk
     *
     * @return rsa jwk
     */
    private RsaJsonWebKey getOrDefaultClientRefreshTokenJWK() {
        final RsaJsonWebKey[] rsaJsonWebKey = new RsaJsonWebKey[1];
        while (true) {
            try {
                if (lockService.runWithLock(AuthLockKeys.REFRESH_TOKEN_CLIENT_KEY_GENERATE, () -> {
                    rsaJsonWebKey[0] = cacheService.get(AuthCacheKey.REFRESH_TOKEN_RSA_JSON_KEY, RsaJsonWebKey.class);
                    if (rsaJsonWebKey[0] == null) {
                        rsaJsonWebKey[0] = JsonWebKeyService.generateRsaJsonWebKey(RsaJsonWebKeyIds.REFRESH_TOKEN_CLIENT_GENERATE);
                    }
                    cacheService.set(AuthCacheKey.REFRESH_TOKEN_RSA_JSON_KEY, rsaJsonWebKey[0]);
                }, 10, TimeUnit.SECONDS)) {
                    break;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("获取 refresh token 生成密钥失败", e);
            }
        }
        return rsaJsonWebKey[0];
    }

    /**
     * @param refreshToken 认证刷新令牌
     * @param clientId     用户客户端唯一号
     * @return 用户认证令牌
     * @throws InvalidSubjectException   非法的主题
     * @throws InvalidJwtException       非法的令牌
     * @throws ExpiredException          令牌已过期
     * @throws InvalidSignatureException 非法的签名
     * @throws InvalidIssuerException    非法的 提供商
     * @throws InvalidAudienceException  非法的使用者
     */
    private String parseClientRefreshToken(String refreshToken, String clientId) throws InvalidSubjectException,
            InvalidJwtException, ExpiredException, InvalidSignatureException, InvalidIssuerException, InvalidAudienceException {
        RsaJsonWebKey rsaJsonWebKey = getOrDefaultClientRefreshTokenJWK();
        return JsonWebTokenService.parseToken(String.class,
                refreshToken,
                TokenPairKeys.ISSUER_REFRESH_TOKEN,
                clientId,
                TokenPairKeys.SUBJECT_REFRESH_TOKEN,
                rsaJsonWebKey);
    }

    /**
     * 校验用户登录信息
     *
     * @param user 用户登录信息
     * @return 校验过的用户信息
     */
    private User checkLoginUserInfo(User user) {
        User savedUser = userManagerService.getByUserLoginInfo(user);
        if (savedUser == null) {
            throw new RuntimeException("缺少用户信息");
        }
        try {
            savedUser.ifUserStatusValid();
        } catch (IllegalUserStatusException e) {
            throw new RuntimeException("用户状态不可用", e);
        }
        savedUser.checkLoginInfo(user);
        return savedUser;
    }

    /**
     * 校验验证码
     *
     * @param userLogin 用户登录信息
     */
    private void checkCaptcha(UserLogin userLogin) {
        String captchaRedisKey = getCaptchaRedisKey(userLogin.getClientId());
        ClickImageTextPointsVerify savedCaptcha = cacheService.get(captchaRedisKey, ClickImageTextPointsVerify.class);
        if (savedCaptcha == null) {
            throw new RuntimeException("验证码已过期！");
        }
        if (savedCaptcha.verifyCaptcha(savedCaptcha.getTextPoints())) {
            throw new RuntimeException("验证码错误！");
        }
    }

    /**
     * 刷新认证信息
     *
     * @param userClientTokenPair 旧的认证信息
     * @return 用户客户端认证信息对
     */
    public UserClientTokenPair refreshAuth(UserClientTokenPair userClientTokenPair) {
        String refreshToken = userClientTokenPair.getRefreshToken();
        String token;
        try {
            token = parseClientRefreshToken(refreshToken, userClientTokenPair.getClientId());
        } catch (InvalidSubjectException | InvalidJwtException | ExpiredException | InvalidSignatureException |
                 InvalidIssuerException | InvalidAudienceException e) {
            throw new RuntimeException(e);
        }
        if (!token.equals(userClientTokenPair.getToken())) {
            throw new RuntimeException("非法的 refresh token");
        }
        while (true) {
            try {
                if (lockService.runWithLock(AuthLockKeys.TOKEN_GENERATE, () -> {
                    // 判断旧的认证是否能用
                    String cachedToken = cacheService.get(getAuthTokenCacheKey(userClientTokenPair.getClientId()), String.class);
                    if (CommonStringUtils.isNotBlank(cachedToken)) {
                        try {
                            Set<String> ignore = parseClientToken(cachedToken, userClientTokenPair.getClientId());
                            userClientTokenPair.setToken(cachedToken);
                            String cachedRefreshToken = cacheService.get(getAuthRefreshTokenCacheKey(userClientTokenPair.getClientId()), String.class);
                            userClientTokenPair.setRefreshToken(cachedRefreshToken);
                            return;
                        } catch (InvalidSubjectException | InvalidJwtException | ExpiredException |
                                 InvalidSignatureException | InvalidIssuerException | InvalidAudienceException ignore) {
                        }
                    }
                    // 生成新的 token 用户认证信息
                    UserClient userClient = userManagerService.getByClientId(userClientTokenPair.getClientId());
                    if (userClient == null) {
                        throw new RuntimeException("客户端不存在");
                    }
                    Set<String> resourceLabels = resourceManagerService.getUserResourceLabels(userClient.getUserCode());
                    String clientToken = generateClientToken(resourceLabels, userClient.getUserClientCode());
                    String clientRefreshToken = generateClientRefreshToken(clientToken, userClient.getUserClientCode());
                    userClientTokenPair.setToken(clientToken);
                    userClientTokenPair.setRefreshToken(clientRefreshToken);
                }, 10, TimeUnit.SECONDS)) {
                    break;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("刷新失败服务异常", e);
            }
        }
        return userClientTokenPair;
    }

    /**
     * 登出
     */
    public void logout(UserClientTokenPair userClientTokenPair) {
        cacheService.remove(getAuthTokenCacheKey(userClientTokenPair.getClientId()),
                getAuthRefreshTokenCacheKey(userClientTokenPair.getClientId()));
    }

}