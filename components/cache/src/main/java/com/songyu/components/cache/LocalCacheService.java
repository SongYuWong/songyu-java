package com.songyu.components.cache;

import com.songyu.commonutils.CommonStringUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 本地缓存实现
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 18:30
 */
public class LocalCacheService implements CacheService {

    /**
     * 缓存上下文容器
     */
    private static final ConcurrentHashMap<String, CacheObjectProxy> CACHE_CONTEXT = new ConcurrentHashMap<>();

    /**
     * 时间 缓存 key map
     */
    private static final ConcurrentHashMap<Long, String> TIME_KEY_MAP = new ConcurrentHashMap<>();

    /**
     * 过期时间有序数组
     */
    private static final LinkedList<Long> EXPIRE_TIME_ARRAY = new LinkedList<>();

    @Override
    public void set(String key, Object payload, long time, TimeUnit timeUnit) {
        CACHE_CONTEXT.put(key, new CacheObjectProxy(key, payload, time, timeUnit));
        clearExpired();
    }

    @Override
    public void set(String key, Object payload) {
        CACHE_CONTEXT.put(key, new CacheObjectProxy(payload));
        clearExpired();
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        CacheObjectProxy cacheObjectProxy = CACHE_CONTEXT.get(key);
        clearExpired();
        if (cacheObjectProxy != null) {
            return cacheObjectProxy.getPayload(clazz);
        }
        return null;
    }

    @Override
    public void remove(String key) {
        CACHE_CONTEXT.remove(key);
    }

    @Override
    public void remove(String... keys) {
        for (String key : keys) {
            CACHE_CONTEXT.remove(key);
        }
        clearExpired();
    }

    synchronized private void clearExpired() {
        Iterator<Long> iterator = EXPIRE_TIME_ARRAY.iterator();
        while (iterator.hasNext()) {
            Long next = iterator.next();
            if (next < System.currentTimeMillis()) {
                iterator.remove();
                String remove = TIME_KEY_MAP.remove(next);
                if (CommonStringUtils.isNotEmpty(remove)) {
                    remove(remove);
                }
            }
        }
    }

    /**
     * 本地缓存对象代理类
     */
    private static class CacheObjectProxy {

        /**
         * 代理目标对象
         */
        private final Object payload;

        /**
         * 缓存过期时间值
         */
        private final Long expireTime;

        private CacheObjectProxy(String key, Object payload, Long expireTime, TimeUnit expireTimeUnit) {
            this.payload = payload;
            if (expireTime == null) {
                this.expireTime = null;
            } else {
                this.expireTime = System.currentTimeMillis() + expireTimeUnit.toMillis(expireTime);
                TIME_KEY_MAP.put(this.expireTime, key);
                EXPIRE_TIME_ARRAY.add(this.expireTime);
            }
        }

        private CacheObjectProxy(Object payload) {
            this.payload = payload;
            this.expireTime = null;
        }

        /**
         * 按类型获取代理目标对象
         *
         * @param clazz 代理目标对象类型
         * @param <T>   代理目标对象类型
         * @return 对应类型的代理目标对象
         */
        public <T> T getPayload(Class<T> clazz) {
            if (expireTime == null) {
                if (payload != null) {
                    return clazz.cast(payload);
                }
            } else {
                if (expireTime > System.currentTimeMillis()) {
                    if (payload != null) {
                        return clazz.cast(payload);
                    }
                }
            }
            return null;
        }
    }

}
