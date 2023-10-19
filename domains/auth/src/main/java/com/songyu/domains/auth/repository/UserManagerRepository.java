package com.songyu.domains.auth.repository;

import com.songyu.domains.auth.entity.User;
import com.songyu.domains.auth.entity.UserClient;
import com.songyu.domains.auth.exception.IllegalUserInfoException;

/**
 * <p>
 * 用户管理存储
 * </p>
 *
 * @author songYu
 * @since 2023/9/20 18:34
 */
public abstract class UserManagerRepository {

    /**
     * 创建新用户
     *
     * @param user 用户信息
     */
    public abstract void addNewUser(User user);

    /**
     * 激活用户
     *
     * @param user 用户信息
     */
    public abstract void activateUser(User user);

    /**
     * 根据用户名获取用户信息
     *
     * @param userName 用户名
     * @return 用户信息
     */
    public abstract User getByUserName(String userName);

    /**
     * 根据用户邮箱获取用户信息
     *
     * @param userEmail 用户邮箱
     * @return 用户信息
     */
    public abstract User getByUserEmail(String userEmail);

    /**
     * 根据用户唯一标识获取用户信息
     *
     * @param user 用户信息
     * @return 数据库中的用户信息
     */
    public abstract User getByUserUniqueInfo(User user) throws IllegalUserInfoException;

    /**
     * 新建用户客户端
     *
     * @param userClient 用户客户端信息
     */
    public abstract void newUserClient(UserClient userClient);

    /**
     * 通过用户客户端唯一号获取用户客户端数据
     *
     * @param clientId 用户客户端唯一号
     * @return 用户客户端数据
     */
    public abstract UserClient getUserClientByClientId(String clientId);
}
