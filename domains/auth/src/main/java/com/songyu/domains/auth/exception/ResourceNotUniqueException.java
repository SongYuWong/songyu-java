package com.songyu.domains.auth.exception;

import com.songyu.domains.auth.entity.Resource;

/**
 * <p>
 * 资源信息不唯一异常
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 11:20
 */
public class ResourceNotUniqueException extends Exception {

    /**
     * 资源信息
     */
    private final Resource resource;

    public ResourceNotUniqueException(Resource resource) {
        this.resource = resource;
    }

    public ResourceNotUniqueException(String message, Resource resource) {
        super(message);
        this.resource = resource;
    }

    public ResourceNotUniqueException(String message, Throwable cause, Resource resource) {
        super(message, cause);
        this.resource = resource;
    }

    public ResourceNotUniqueException(Throwable cause, Resource resource) {
        super(cause);
        this.resource = resource;
    }

    public ResourceNotUniqueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Resource resource) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.resource = resource;
    }

}
