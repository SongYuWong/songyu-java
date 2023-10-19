package com.songyu.components.api;

import cn.hutool.json.JSONUtil;
import com.songyu.commonutils.CommonStringUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 认证请求代理
 * </p>
 *
 * @author songYu
 * @since 2023/9/14 17:07
 */
@Setter
public class SecureRequest {

    /**
     * 请求数据
     */
    private String data;

    /**
     * 发送方公钥
     */
    @Getter
    private String key;

    public <T> T parseRequest(ApiSecureManager apiSecureManager, Class<T> clazz) {
        if (CommonStringUtils.isBlank(data)) {
            Response.throwRequestError("请求数据缺失！");
        }
        if (CommonStringUtils.isBlank(key)) {
            Response.throwRequestError("不安全的请求！");
        }
        if (CommonStringUtils.isBlank(data)) {
            Response.throwRequestError("请求数据缺失！");
        }
        return JSONUtil.toBean(apiSecureManager.decryptSecureDataStr(this.data, this.key), clazz);
    }

}
