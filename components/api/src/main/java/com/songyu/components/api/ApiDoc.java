package com.songyu.components.api;

import java.lang.annotation.*;

/**
 * 接口文档注解
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiDoc {

    /**
     * 接口名称
     *
     * @return 接口名称
     */
    String name();

    /**
     * 接口描述
     *
     * @return 接口描述
     */
    String desc();

    /**
     * 请求类型
     *
     * @return 类型
     */
    Class<?> requestType() default Object.class;

    /**
     * 响应类型
     *
     * @return 类型
     */
    Class<?> responseType() default Object.class;

}
