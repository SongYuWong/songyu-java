package com.songyu.components.springboot.redis;

import com.songyu.components.cache.CacheService;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * redis 操作相关业务
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 14:26
 */
public class RedisService implements CacheService {

    private final RedisTemplate<String, Object> jsonValueRedisTemplate;

    public RedisService(RedisTemplate<String, Object> jsonValueRedisTemplate) {
        this.jsonValueRedisTemplate = jsonValueRedisTemplate;
    }

    @Override
    public void set(String key, Object payload, long time, TimeUnit timeUnit) {
        jsonValueRedisTemplate.opsForValue().set(key, payload, time, timeUnit);
    }

    @Override
    public void set(String key, Object payload) {
        jsonValueRedisTemplate.opsForValue().set(key, payload);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        Object o = jsonValueRedisTemplate.opsForValue().get(key);
        return clazz.cast(o);
    }

    @Override
    public void remove(String key) {
        jsonValueRedisTemplate.delete(key);
    }

    @Override
    public void remove(String... keys) {
        jsonValueRedisTemplate.delete(Arrays.asList(keys));
    }

}
