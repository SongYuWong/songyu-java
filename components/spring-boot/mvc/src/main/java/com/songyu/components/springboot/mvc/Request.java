package com.songyu.components.springboot.mvc;

import lombok.Data;

/**
 * <p>
 * 请求封装
 * </p>
 *
 * @author songYu
 * @since 2023/8/24 15:48
 */
@Data
public class Request {

    /**
     * 请求令牌
     */
    private String token;

    /**
     * 请求数据
     */
    private Object data;

}
