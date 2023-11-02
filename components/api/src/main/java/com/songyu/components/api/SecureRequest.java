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

    /**
     * 解密安全请求
     *
     * @param apiSecureManager 服务端安全管理对象
     * @param clazz            解密后的数据类型
     * @param <T>              数据类型
     * @return 解密后的数据
     */
    public <T> T parseRequest(ApiSecureManager apiSecureManager, Class<T> clazz) {
        validRequestInfo();
        return JSONUtil.toBean(apiSecureManager.decryptSecureDataStrSigned(this.data, this.key), clazz);
    }

    /**
     * 验证请求签名
     *
     * @param apiSecureManager 服务端安全管理对象
     * @param clazz            解密后的数据类型
     * @param <T>              数据类型
     * @return 解密后的数据
     */
    public <T> T verifyRequestSign(ApiSecureManager apiSecureManager, Class<T> clazz) {
        validRequestInfo();
        return JSONUtil.toBean(apiSecureManager.verifySecureDataStrSign(this.data, this.key), clazz);
    }

    /**
     * 校验请求信息
     */
    private void validRequestInfo() {
        if (CommonStringUtils.isBlank(data)) {
            Response.throwRequestError("请求数据缺失！");
        }
        if (CommonStringUtils.isBlank(key)) {
            Response.throwRequestError("不安全的请求！");
        }
    }


}
