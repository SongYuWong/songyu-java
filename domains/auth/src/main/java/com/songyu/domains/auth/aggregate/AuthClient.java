package com.songyu.domains.auth.aggregate;

import com.songyu.components.captcha.clickimagetext.ClickImageTextPointsVerify;
import com.songyu.domains.auth.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 认证客户端
 * </p>
 *
 * @author songYu
 * @since 2023/11/2 11:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthClient extends User {

    private String clientId;

    private String token;

    private String refreshToken;

    private ClickImageTextPointsVerify.TextPoint[] captcha;

    private String activeCode;

    private String clientKey;

}
