package com.songyu.components.lock;

/**
 * <p>
 * 原子执行函数式接口
 * </p>
 *
 * @author songYu
 * @since 2023/9/26 9:08
 */
@FunctionalInterface
public interface Atomic {

    void run();

}
