package com.songyu.components.captcha;

/**
 * <p>
 * 校验验证码信息
 * </p>
 *
 * @author songYu
 * @since 2023/9/22 15:59
 */
public abstract class VerifyInfo<P> {

    /**
     * 校验验证码
     *
     * @return 验证码是否通过校验
     */
    public abstract boolean verifyCaptcha(P p);

}
