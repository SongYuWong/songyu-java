package com.songyu.components.captcha;

/**
 * <p>
 * 验证码工厂类
 * </p>
 *
 * @author songYu
 * @since 2023/9/22 9:05
 */
public class CaptchaFactory {

    /**
     * 生成验证码
     *
     * @param captchaType 验证码类型
     * @param seeds       种子串
     * @return 验证码
     */
    public static Captcha generatorCaptcha(CaptchaType captchaType, String seeds) {
        return captchaType.getCaptchaInitiator().apply(seeds);
    }

}
