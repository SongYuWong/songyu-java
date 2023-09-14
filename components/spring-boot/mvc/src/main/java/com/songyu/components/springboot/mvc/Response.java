package com.songyu.components.springboot.mvc;

import lombok.Data;

/**
 * <p>
 * 响应封装
 * </p>
 *
 * @author songYu
 * @since 2023/8/24 15:47
 */
@Data
public class Response {

    /**
     * 响应码
     */
    private String code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应摘要
     */
    private String summary;

    /**
     * 响应数据
     */
    private Object data;

}
