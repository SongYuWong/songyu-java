package com.songyu.components.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

/**
 * <p>
 * 认证相应代理
 * </p>
 *
 * @author songYu
 * @since 2023/9/14 17:16
 */
@Data
public class SecureResponse<T> {

    /**
     * 响应编码
     */
    private Integer code;

    /**
     * 认证响应数据
     */
    private String data;

    /**
     * 响应方公钥
     */
    private String key;

    /**
     * 结果数据类型
     */
    private Class<T> clazz;

    /**
     * 从普通的 mvc 响应构建用于认证的响应
     *
     * @param response  mvc 统一响应
     * @param publicKey 接收端公钥
     * @return 认证统一响应
     */
    public static <T> SecureResponse<T> buildWithResponse(ApiSecureManager apiSecureManager, Response<T> response, String publicKey) {
        SecureResponse<T> responseAuthorized = new SecureResponse<>();
        //noinspection unchecked
        responseAuthorized.setClazz(response.getData() == null ? null : (Class<T>) response.getData().getClass());
        responseAuthorized.setCode(response.getCode());
        responseAuthorized.setKey(apiSecureManager.getPublicKeyStr());
        String securityDataStr = apiSecureManager.encryptSecurityDataStr(JSON.toJSONString(response, SerializerFeature.PrettyFormat), publicKey);
        responseAuthorized.setData(securityDataStr);
        return responseAuthorized;
    }

    public Response<T> parseResponse(ApiSecureManager apiSecureManager) {
        Response<?> bean = JSON.parseObject(apiSecureManager.decryptSecureDataStrSigned(this.data, this.key), Response.class);
        return new Response<>(bean, this.clazz);
    }

}
