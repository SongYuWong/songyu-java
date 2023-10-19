package com.songyu.components.lock.local;

import com.songyu.components.lock.Lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 本地锁实现
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 10:03
 */
public class LocalLock implements Lock {

    private final ReentrantLock reentrantLock = new ReentrantLock();

    @Override
    public boolean tryLock() {
        return reentrantLock.tryLock();
    }

    @Override
    public boolean tryLock(long waitTimeOut, TimeUnit timeUnit) throws InterruptedException {
        return reentrantLock.tryLock(waitTimeOut, timeUnit);
    }

    @Override
    public void lock() {
        reentrantLock.lock();
    }

    @Override
    public void unLock() {
        reentrantLock.unlock();
    }

    @Override
    public void close() {
        reentrantLock.unlock();
    }

}
