package com.songyu.components.captcha;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 验证码抽象
 * </p>
 *
 * @author songYu
 * @since 2023/9/22 9:06
 */
@Getter
public abstract class Captcha {

    /**
     * base64 格式背景图片
     */
    protected String base64BGStr;

    /**
     * 验证码种子
     */
    private final String seeds;

    /**
     * 验证信息
     */
    protected VerifyInfo<?> verifyInfo;

    public Captcha(String seeds) {
        this.seeds = seeds;
        initCaptcha();
    }

    /**
     * 初始化验证码
     */
    protected abstract void initCaptcha();

    /**
     * 清除验证码中携带的验证信息
     */
    public Captcha clearVerifyInfo() {
        this.verifyInfo = null;
        return this;
    }
}
