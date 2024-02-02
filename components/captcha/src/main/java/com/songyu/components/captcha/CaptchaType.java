package com.songyu.components.captcha;

import com.songyu.components.captcha.clickimagetext.ClickImageTextCaptcha;
import lombok.Getter;

import java.util.function.Function;

/**
 * <p>
 * 验证码类型
 * </p>
 *
 * @author songYu
 * @since 2023/9/22 9:09
 */
@Getter
public enum CaptchaType {

    /**
     * 图片文字点选
     */
    CLICK_IMAGE_TEXT(ClickImageTextCaptcha::new);

    private final Function<String, Captcha> captchaInitiator;

    CaptchaType(Function<String, Captcha> captchaInitiator) {
        this.captchaInitiator = captchaInitiator;
    }

}
