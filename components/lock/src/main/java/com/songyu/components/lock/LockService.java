package com.songyu.components.lock;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 锁业务
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 8:55
 */
public abstract class LockService<T extends Lock> {

    public abstract T getLock(Object key);

    public void runWithLock(Object key, Atomic atomic) {
        T lock = getLock(key);
        lock.lock();
        atomic.run();
        lock.unLock();
    }

    public boolean runWithLock(Object key, Atomic atomic, int waitTime, TimeUnit timeUnit) throws InterruptedException {
        T lock = getLock(key);
        if (lock.tryLock(waitTime, timeUnit)) {
            atomic.run();
            lock.unLock();
            return true;
        } else {
            return false;
        }
    }

}
