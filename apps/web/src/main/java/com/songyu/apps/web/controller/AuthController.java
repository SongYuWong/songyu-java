package com.songyu.apps.web.controller;

import com.songyu.components.captcha.Captcha;
import com.songyu.components.api.Response;
import com.songyu.components.api.ResponseStatus;
import com.songyu.components.api.SecureRequest;
import com.songyu.components.api.SecureResponse;
import com.songyu.components.springboot.mvc.utils.RequestParamChecker;
import com.songyu.components.api.ApiSecureManager;
import com.songyu.domains.auth.aggregate.AuthClient;
import com.songyu.domains.auth.aggregate.UserLogin;
import com.songyu.domains.auth.aggregate.UserRegistered;
import com.songyu.domains.auth.entity.User;
import com.songyu.domains.auth.entity.UserClientTokenPair;
import com.songyu.domains.auth.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 认证接口
 * </p>
 *
 * @author songYu
 * @since 2023/9/20 14:25
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ApiSecureManager apiSecureManager;

    private final AuthService authService;

    public AuthController(ApiSecureManager apiSecureManager,
                          AuthService authService) {
        this.authService = authService;
        this.apiSecureManager = apiSecureManager;
    }

    @PostMapping("/signup")
    public SecureResponse<Void> signup(@RequestBody SecureRequest userSecureRequest) {
        User user = userSecureRequest.parseRequest(apiSecureManager, User.class);
        RequestParamChecker.notNull(user, "缺少注册的用户信息。");
        authService.signup(user);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, null),
                userSecureRequest.getKey()
        );
    }

    @PostMapping("/activation")
    public SecureResponse<Void> activation(@RequestBody SecureRequest userSecureRequest) {
        UserRegistered userRegistered = userSecureRequest.parseRequest(apiSecureManager, UserRegistered.class);
        RequestParamChecker.notNull(userRegistered, "缺少注册的用户信息。");
        authService.activationUser(userRegistered);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, null),
                userSecureRequest.getKey());
    }

    @PostMapping("/captcha")
    public SecureResponse<Captcha> captcha(@RequestBody SecureRequest userClientSecureRequest) {
        UserLogin userLogin = userClientSecureRequest.parseRequest(apiSecureManager, UserLogin.class);
        Captcha captcha = authService.generateCaptcha(userLogin);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, captcha),
                userClientSecureRequest.getKey()
        );
    }

    @PostMapping("/verifyCaptcha")
    public SecureResponse<Boolean> verifyCaptcha(@RequestBody SecureRequest userClientSecureRequest) {
        AuthClient authClient = userClientSecureRequest.parseRequest(apiSecureManager, AuthClient.class);
        authService.checkCaptcha(authClient);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, null),
                userClientSecureRequest.getKey()
        );
    }

    @PostMapping("/login")
    public SecureResponse<UserClientTokenPair> login(@RequestBody SecureRequest userSecureRequest) {
        UserLogin userLogin = userSecureRequest.parseRequest(apiSecureManager, UserLogin.class);
        RequestParamChecker.notNull(userLogin, "缺少登录信息。");
        userLogin.setClientKey(userSecureRequest.getKey());
        UserClientTokenPair userClientTokenPair = authService.login(userLogin);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, userClientTokenPair),
                userSecureRequest.getKey()
        );
    }

    @PostMapping("/refreshAuth")
    public SecureResponse<UserClientTokenPair> refreshAuth(
            @RequestBody SecureRequest userClientTokenPairSecureRequest) {
        UserClientTokenPair userClientTokenPair = userClientTokenPairSecureRequest.parseRequest(apiSecureManager, UserClientTokenPair.class);
        RequestParamChecker.notNull(userClientTokenPair, "缺少认证刷新信息。");
        UserClientTokenPair userClientTokenPairNew = authService.refreshAuth(userClientTokenPair);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, userClientTokenPairNew),
                userClientTokenPairSecureRequest.getKey()
        );
    }

    @PostMapping("/logout")
    public SecureResponse<Void> logout(@RequestBody SecureRequest userClientTokenPairSecureRequest) {
        UserClientTokenPair userClientTokenPair = userClientTokenPairSecureRequest.parseRequest(apiSecureManager, UserClientTokenPair.class);
        RequestParamChecker.notNull(userClientTokenPair, "缺少认证刷新信息。");
        authService.logout(userClientTokenPair);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, null),
                userClientTokenPairSecureRequest.getKey());
    }

    @PostMapping("/serverPublicKey")
    public SecureResponse<String> serverPublicKey(@RequestBody SecureRequest serverPublicKey) {
        AuthClient ignored = serverPublicKey.verifyRequestSign(this.apiSecureManager, AuthClient.class);
        return SecureResponse.buildWithResponse(
                apiSecureManager,
                Response.buildWithPayload(ResponseStatus.SUCCESS, apiSecureManager.getPublicKeyStr()),
                serverPublicKey.getKey()
        );
    }

}
