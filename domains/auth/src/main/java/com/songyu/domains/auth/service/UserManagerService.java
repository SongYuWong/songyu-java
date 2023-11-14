package com.songyu.domains.auth.service;

import com.songyu.commonutils.CommonStringUtils;
import com.songyu.domains.auth.entity.User;
import com.songyu.domains.auth.entity.UserClient;
import com.songyu.domains.auth.exception.IllegalUserInfoException;
import com.songyu.domains.auth.exception.IllegalUserStatusException;
import com.songyu.domains.auth.exception.UserNotUniqueException;
import com.songyu.domains.auth.repository.UserManagerRepository;
import com.songyu.domains.auth.valueObject.UserStatus;

/**
 * <p>
 * 用户管理相关业务
 * </p>
 *
 * @author songYu
 * @since 2023/9/25 16:29
 */
public abstract class UserManagerService {

    private final UserManagerRepository userManagerRepository;

    public UserManagerService(UserManagerRepository userManagerRepository) {
        this.userManagerRepository = userManagerRepository;
    }

    /**
     * 创建新用户
     *
     * @param user 用户信息
     * @throws IllegalUserInfoException 非法的用户信息
     * @throws UserNotUniqueException   用户信息不符合唯一性约束
     */
    public void createNewUser(User user) throws IllegalUserInfoException,
            UserNotUniqueException {
        user.checkIfPrimaryInfoComplete();
        User selectedUser = userManagerRepository.getByUserAnyUniqueInfo(user);
        if (selectedUser != null) {
            if (!UserStatus.statusRegistering(selectedUser.getUserStatusCode())) {
                if (user.getUserName().equals(selectedUser.getUserName())) {
                    throw new UserNotUniqueException("用户名已被使用", user);
                } else if (user.getUserEmail().equals(selectedUser.getUserEmail())) {
                    throw new UserNotUniqueException("用户邮箱已被使用", user);
                } else {
                    throw new UserNotUniqueException(user);
                }
            }
        } else {
            userManagerRepository.addNewUser(user);
        }
    }

    /**
     * 激活用户
     *
     * @param user 用户信息
     */
    public void activateUser(User user) {
        User selectedUser;
        try {
            selectedUser = userManagerRepository.getByUserUniqueInfo(user);
        } catch (IllegalUserInfoException e) {
            throw new RuntimeException("缺少用户唯一性标识。", e);
        }
        if (selectedUser == null) {
            throw new RuntimeException("未找到用户信息！");
        } else {
            try {
                selectedUser.ifUserStatusValid();
            } catch (IllegalUserStatusException e) {
                if (!UserStatus.REGISTERED.equals(e.getUserStatus())) {
                    if (UserStatus.BLACK.equals(e.getUserStatus())) {
                        throw new RuntimeException("非法的用户！");
                    } else {
                        throw new RuntimeException("用户已激活！请到登录页直接登录。");
                    }
                }
            }
        }
        userManagerRepository.activateUser(user);
    }

    /**
     * 添加用户客户端
     *
     * @param userClient 用户客户端信息
     */
    public void addNewUserClient(UserClient userClient) {
        userManagerRepository.newUserClient(userClient);
    }

    /**
     * 通过用户登录账号信息获取用户（用户名或者邮箱）
     *
     * @param user 用户信息
     * @return 保存的用户信息
     */
    public User getByUserLoginInfo(User user) {
        if (CommonStringUtils.isNotBlank(user.getUserCode())) {
            return userManagerRepository.getByUserNameOrEmail(user.getUserCode());
        } else {
            throw new RuntimeException("缺少用户账户信息");
        }
    }

    public UserClient getByClientId(String clientId) {
        return userManagerRepository.getUserClientByClientId(clientId);
    }

}
