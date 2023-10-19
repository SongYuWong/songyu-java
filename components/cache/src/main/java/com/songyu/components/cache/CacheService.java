package com.songyu.components.cache;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 缓存相关业务
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 18:27
 */
public interface CacheService {

    /**
     * 设置缓存并设置过期时间
     *
     * @param key      缓存 key
     * @param payload  缓存的对象
     * @param time     过期时间值
     * @param timeUnit 过期时间单位
     */
    void set(String key, Object payload, long time, TimeUnit timeUnit);

    /**
     * 设置缓存
     *
     * @param key     缓存 key
     * @param payload 缓存对象
     */
    void set(String key, Object payload);

    /**
     * 获取缓存的对象
     *
     * @param key   缓存 key
     * @param clazz 缓存对象的类型
     * @param <T>   缓存对象的类型
     * @return 缓存的对象
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 删除缓存
     *
     * @param key 要删除的缓存的 key
     */
    void remove(String key);

    /**
     * 删除缓存
     *
     * @param keys 要删除的缓存的 key 列表值
     */
    void remove(String... keys);

}
