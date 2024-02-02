package com.songyu.components.springboot.data;

/**
 * <p>
 *
 * </p>
 *
 * @author songYu
 * @since 2023/12/7 0:27
 */
public interface ITable {

    /**
     * 加密信息用于持久化
     */
    void encryptInfo();

    /**
     * 解密持久化的信息
     */
    void decryptInfo();
}
