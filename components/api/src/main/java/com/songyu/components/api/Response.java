package com.songyu.components.api;

import com.songyu.components.api.exception.*;
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
public class Response<T> {

    /**
     * 响应码
     */
    private Integer code;

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
    private T data;

    public Response(Response<?> bean, Class<T> clazz) {
        this.code = bean.getCode();
        this.message = bean.getMessage();
        this.summary = bean.getSummary();
        this.data = clazz.cast(bean.getData());
    }

    public Response(Integer code, String message, String summary, T data) {
        this.code = code;
        this.message = message;
        this.summary = summary;
        this.data = data;
    }

    public static <T> Response<T> build(ResponseStatus responseStatus) {
        return new Response<>(responseStatus.ordinal(), responseStatus.getMessage(), responseStatus.getSummary(), null);
    }

    public static <T> Response<T> buildWithMessage(ResponseStatus responseStatus, String message) {
        return new Response<>(responseStatus.ordinal(), message, responseStatus.getSummary(), null);
    }

    public static <T> Response<T> buildWithSummary(ResponseStatus responseStatus, String summary) {
        return new Response<>(responseStatus.ordinal(), responseStatus.getMessage(), summary, null);
    }

    public static <T> Response<T> buildWithPayload(ResponseStatus responseStatus, T payload) {
        return new Response<>(responseStatus.ordinal(), responseStatus.getMessage(), responseStatus.getSummary(), payload);
    }

    public static <T> Response<T> build(ResponseStatus responseStatus, String message, String summary) {
        return new Response<>(responseStatus.ordinal(), message, summary, null);
    }

    public static <T> Response<T> buildWithMessage(ResponseStatus responseStatus, String message, T payload) {
        return new Response<>(responseStatus.ordinal(), message, message, payload);
    }

    public static <T> Response<T> buildWithSummary(ResponseStatus responseStatus, String summary, T payload) {
        return new Response<>(responseStatus.ordinal(), responseStatus.getMessage(), summary, payload);
    }

    public static <T> Response<T> build(ResponseStatus responseStatus, String message, String summary, T payload) {
        return new Response<>(responseStatus.ordinal(), message, summary, payload);
    }

    public static void throwRequestError() {
        throw new RequestErrorException();
    }

    public static void throwRequestError(String message) {
        throw new RequestErrorException(message);
    }

    public static void throwRequestError(String message, Throwable e) {
        throw new RequestErrorException(message, e);
    }

    public static void throwServerError() {
        throw new ServerErrorException();
    }

    public static void throwServerError(String message) {
        throw new ServerErrorException(message);
    }

    public static void throwServerError(String message, Throwable e) {
        throw new ServerErrorException(message, e);
    }

    public static void throwBizError() {
        throw new BizErrorException();
    }

    public static void throwBizError(String message) {
        throw new BizErrorException(message);
    }

    public static void throwBizError(String message, Throwable e) {
        throw new BizErrorException(message, e);
    }

    public static void throwNoAuth() {
        throw new NoAuthException();
    }

    public static void throwNoAuth(String message) {
        throw new NoAuthException(message);
    }

    public static void throwNoAuth(String message, Throwable e) {
        throw new NoAuthException(message, e);
    }

    public static void throwNoAccess() {
        throw new NoAccessException();
    }

    public static void throwNoAccess(String message) {
        throw new NoAccessException(message);
    }

    public static void throwNoAccess(String message, Throwable e) {
        throw new NoAccessException(message, e);
    }

}
