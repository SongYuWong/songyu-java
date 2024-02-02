package com.songyu.domains.auth.repository;

import com.mybatisflex.core.paginate.Page;
import com.songyu.domains.auth.aggregate.UserPage;
import com.songyu.domains.auth.entity.User;
import com.songyu.domains.auth.entity.UserClient;
import com.songyu.domains.auth.exception.IllegalUserInfoException;

/**
 * <p>
 * 用户存储
 * </p>
 *
 * @author songYu
 * @since 2023/9/20 18:34
 */
public abstract class UserRepository {

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
     * 根据任意用户唯一标识获取用户信息
     *
     * @param user 用户信息
     * @return 数据库中的用户信息
     */
    public abstract User getByUserAnyUniqueInfo(User user) throws IllegalUserInfoException;

    /**
     * 根据用户唯一标识获取用户信息
     *
     * @param user 用户信息
     * @return 数据库中的用户信息
     */
    public abstract User getByUserUniqueInfo(User user) throws IllegalUserInfoException;

    /**
     * 保存或更新用户客户端
     *
     * @param userClient 用户客户端信息
     */
    public abstract void saveOrUpdateUserClient(UserClient userClient);

    /**
     * 通过用户客户端唯一号获取用户客户端数据
     *
     * @param clientId 用户客户端唯一号
     * @return 用户客户端数据
     */
    public abstract UserClient getUserClientByClientId(String clientId);

    /**
     * 根据用户名或者邮箱获取保存了的用户信息
     *
     * @param userNameOrEmail 用户名或邮箱
     * @return 持久化了的用户信息
     */
    public abstract User getByUserNameOrEmail(String userNameOrEmail);

    /**
     * 根据客户端唯一号获取用户
     *
     * @param clientId 客户端唯一号
     * @return 客户端对应的用户信息
     */
    public abstract User getUserByClientId(String clientId);

    /**
     * 清除用户客户端数据
     *
     * @param clientId 客户端ID
     */
    public abstract void clearUserClient(String clientId);

    /**
     * 根据分页信息获取用户分页
     *
     * @param userPage 分页信息
     * @return 用户分页数据
     */
    public abstract Page<UserPage> userPage(UserPage userPage);

    /**
     * 编辑用户
     *
     * @param user 用户信息
     */
    public abstract void editUser(User user);

    /**
     * 根据用户编码获取用户信息
     *
     * @param userCode 用户编码
     * @return 用户信息
     */
    public abstract User getByUserCode(String userCode);

    /**
     * 禁用用户
     *
     * @param user 用户信息
     */
    public abstract void disableUser(User user);

}
