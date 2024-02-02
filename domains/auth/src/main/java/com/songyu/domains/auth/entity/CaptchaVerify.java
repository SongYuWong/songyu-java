package com.songyu.domains.auth.entity;

import com.songyu.components.captcha.clickimagetext.ClickImageTextPointsVerify;
import lombok.Data;

/**
 * <p>
 * 验证码信息
 * </p>
 *
 * @author songYu
 * @since 2024/1/16 11:45
 */
@Data
public class CaptchaVerify {

    /**
     * 客户端唯一编码
     */
    private String clientId;

    /**
     * 客户端验证码
     */
    private ClickImageTextPointsVerify.TextPoint[] points;

}
