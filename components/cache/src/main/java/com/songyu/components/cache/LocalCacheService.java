package com.songyu.components.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
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
    private static final CopyOnWriteArrayList<Long> EXPIRE_TIME_ARRAY = new CopyOnWriteArrayList<>();

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
        clearExpired();
        CacheObjectProxy cacheObjectProxy = CACHE_CONTEXT.get(key);
        if (cacheObjectProxy != null) {
            return cacheObjectProxy.getPayload(clazz);
        }
        return null;
    }

    @Override
    public void remove(String key) {
        CACHE_CONTEXT.remove(key);
        clearExpired();
    }

    @Override
    public void remove(String... keys) {
        for (String key : keys) {
            CACHE_CONTEXT.remove(key);
        }
        clearExpired();
    }

    private void clearExpired() {
        int index = -1;
        for (int i = 0; i < EXPIRE_TIME_ARRAY.size(); i++) {
            if (EXPIRE_TIME_ARRAY.get(i) < System.currentTimeMillis()) {
                index = i;
                remove(TIME_KEY_MAP.remove(EXPIRE_TIME_ARRAY.get(i)));
            } else {
                break;
            }
        }
        if (index >= 0) {
            for (int i = -1; i < index; ) {
                EXPIRE_TIME_ARRAY.remove(++i);
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
                EXPIRE_TIME_ARRAY.add(0, this.expireTime);
                insertionSort();
            }
        }

        private void insertionSort() {
            if (LocalCacheService.EXPIRE_TIME_ARRAY.size() <= 1) {
                return;
            }
            int n = LocalCacheService.EXPIRE_TIME_ARRAY.size();
            for (int i = 1; i < n; i++) {
                long key = LocalCacheService.EXPIRE_TIME_ARRAY.get(i);
                int j = i - 1;
                while (j >= 0 && LocalCacheService.EXPIRE_TIME_ARRAY.get(j) > key) {
                    LocalCacheService.EXPIRE_TIME_ARRAY.set(j + 1, LocalCacheService.EXPIRE_TIME_ARRAY.get(j));
                    j--;
                }
                LocalCacheService.EXPIRE_TIME_ARRAY.set(j + 1, key);
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
                if (expireTime < System.currentTimeMillis()) {
                    if (payload != null) {
                        return clazz.cast(payload);
                    }
                }
            }
            return null;
        }
    }

}
