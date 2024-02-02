package com.songyu.components.api;

import lombok.Getter;

/**
 * <p>
 * 相应状态码枚举
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 18:17
 */
public enum ResponseStatus {

    /**
     * 成功响应
     */
    SUCCESS("成功", "请求成功"),

    /**
     * 请求异常
     */
    REQUEST_ERR("失败", "请求异常"),

    /**
     * 服务异常
     */
    SERVER_ERR("失败", "服务异常"),

    /**
     * 业务异常
     */
    BIZ_ERR("失败", "业务异常"),

    /**
     * 没有认证
     */
    NO_AUTH("失败", "没有认证"),

    /**
     * 没有权限
     */
    NO_ACCESS("失败", "没有权限");

    @Getter
    private final String message;

    @Getter
    private final String summary;

    ResponseStatus(String message, String summary) {
        this.message = message;
        this.summary = summary;
    }

}
