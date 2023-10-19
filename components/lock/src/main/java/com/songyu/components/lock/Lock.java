package com.songyu.components.lock;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 锁的抽象
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 9:03
 */
public interface Lock extends AutoCloseable{

    boolean tryLock();

    boolean tryLock(long waitTimeOut, TimeUnit timeUnit) throws InterruptedException;

    void lock();

    void unLock();

}
