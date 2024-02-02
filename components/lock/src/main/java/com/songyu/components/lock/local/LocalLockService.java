package com.songyu.components.lock.local;
import com.songyu.components.lock.LockService;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 10:03
 */
public class LocalLockService extends LockService<LocalLock> {

    private static final ConcurrentHashMap<Object, LocalLock> LOCK_CONTEXT = new ConcurrentHashMap<>();

    @Override
    public LocalLock getLock(Object key) {
        return LOCK_CONTEXT.computeIfAbsent(key, o -> new LocalLock());
    }

}
