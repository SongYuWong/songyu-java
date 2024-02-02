package com.songyu.domains.auth.service;

import com.mybatisflex.core.paginate.Page;
import com.songyu.commonutils.CommonAESEncryptUtils;
import com.songyu.commonutils.CommonStringUtils;
import com.songyu.domains.auth.aggregate.UserLogin;
import com.songyu.domains.auth.aggregate.UserPage;
import com.songyu.domains.auth.entity.User;
import com.songyu.domains.auth.entity.UserClient;
import com.songyu.domains.auth.exception.IllegalUserInfoException;
import com.songyu.domains.auth.exception.IllegalUserStatusException;
import com.songyu.domains.auth.exception.UserNotUniqueException;
import com.songyu.domains.auth.repository.UserRepository;
import com.songyu.domains.auth.valueObject.UserStatus;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * <p>
 * 用户管理相关业务
 * </p>
 *
 * @author songYu
 * @since 2023/9/25 16:29
 */
public abstract class UserManagerService {

    @Resource
    private UserRepository userRepository;

    /**
     * 用户名密钥
     */
    private static final String UserNameKey = "abDn4R1LwAJG6aheSEE1p2O";

    /**
     * 用户邮箱密钥
     */
    private static final String UserEmailKey = "abDn4R1LwAJG6aheSEuNFAO";

    /**
     * 创建新用户
     *
     * @param user 用户信息
     * @throws IllegalUserInfoException 非法的用户信息
     * @throws UserNotUniqueException   用户信息不符合唯一性约束
     */
    public void createNewUser(User user) throws IllegalUserInfoException,
            UserNotUniqueException {
        checkUserUnique(user);
        userRepository.addNewUser(user);
    }

    /**
     * 检查用户业务唯一性
     *
     * @param user 用户信息
     */
    public void checkUserUnique(User user) throws IllegalUserInfoException,
            UserNotUniqueException {
        user.checkIfPrimaryInfoComplete();
        User selectedUser = userRepository.getByUserAnyUniqueInfo(user);
        if (selectedUser != null) {
            if (!User.statusRegistering(selectedUser.getUserStatusCode())) {
                if (user.getUserName().equals(selectedUser.getUserName())) {
                    throw new UserNotUniqueException("用户名已被使用", user);
                } else if (user.getUserEmail().equals(selectedUser.getUserEmail())) {
                    throw new UserNotUniqueException("用户邮箱已被使用", user);
                } else {
                    throw new UserNotUniqueException(user);
                }
            }
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
            selectedUser = userRepository.getByUserUniqueInfo(user);
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
        userRepository.activateUser(user);
    }

    /**
     * 添加用户客户端
     *
     * @param userClientCode         用户客户端唯一号
     * @param userCode               用户唯一编码
     * @param clientAuthRefreshToken 用户认证刷新令牌
     */
    public void recordUserClient(String userClientCode, String userCode, String clientAuthRefreshToken) {
        UserClient userClient = new UserClient();
        userClient.setUserClientRefreshToken(clientAuthRefreshToken);
        userClient.setUserCode(userCode);
        userClient.setUserClientCode(userClientCode);
        userRepository.saveOrUpdateUserClient(userClient);
    }

    /**
     * 通过用户登录账号信息获取用户（用户名或者邮箱）
     *
     * @param user 用户信息
     * @return 保存的用户信息
     */
    public User getByUserLoginInfo(User user) {
        if (CommonStringUtils.isNotBlank(user.getUserCode())) {
            return userRepository.getByUserNameOrEmail(
                    CommonAESEncryptUtils.encryptObject(user.getUserCode(),
                            CommonStringUtils.isValidEmail(user.getUserCode()) ? UserEmailKey : UserNameKey));
        } else {
            throw new RuntimeException("缺少用户账户信息");
        }
    }

    /**
     * 根据客户端 id 获取用户客户端信息
     *
     * @param clientId 客户端 id
     * @return 用户客户端信息
     */
    public UserClient getUserClientByClientId(String clientId) {
        return userRepository.getUserClientByClientId(clientId);
    }

    /**
     * 根据客户端 id 获取用户信息
     *
     * @param clientId 客户端 id
     * @return 用户信息
     */
    public User getUserByClientId(String clientId) {
        return userRepository.getUserByClientId(clientId);
    }

    /**
     * 清除用户客户端信息
     *
     * @param clientId 客户端唯一号
     */
    public void clearUserClient(String clientId) {
        userRepository.clearUserClient(clientId);
    }

    /**
     * 根据客户端唯一号获取认证刷新令牌
     *
     * @param userLogin 用户登录信息
     * @return 认证刷新令牌
     */
    public String getRefreshTokenByClientId(UserLogin userLogin) {
        UserClient userClientByClientId = userRepository.getUserClientByClientId(userLogin.getClientId());
        if (userClientByClientId == null) {
            return null;
        }
        return userClientByClientId.getUserClientRefreshToken();
    }

    /**
     * 管理新增用户
     *
     * @param user 新增用户的用户信息
     * @return 新增的用户信息
     */
    public User newUser(User user) {
        try {
            checkUserUnique(user);
        } catch (IllegalUserInfoException | UserNotUniqueException e) {
            throw new RuntimeException(e);
        }
        user.setUserStatusCode(UserStatus.NORMAL.ordinal());
        userRepository.addNewUser(user);
        return userRepository.getByUserName(user.getUserName());
    }

    /**
     * 用户归档状态禁用清除所有客户端
     *
     * @param user 用户信息
     */
    public void archiveUser(User user) {
        userRepository.disableUser(user);
    }

    /**
     * 管理编辑用户
     *
     * @param user 用户信息
     * @return 更改后的用户信息
     */
    public User editUser(User user) {
        try {
            User selectedUser = userRepository.getByUserAnyUniqueInfo(user);
            if (!Objects.equals(selectedUser.getUserCode(), user.getUserCode())) {
                throw new RuntimeException("邮箱已被其他账户绑定");
            } else {
                userRepository.editUser(user);
                return userRepository.getByUserCode(user.getUserCode());
            }
        } catch (IllegalUserInfoException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据分页信息获取用户分页
     *
     * @param userPage 分页信息
     * @return 用户分页数据
     */
    public Page<UserPage> userPage(UserPage userPage) {
        return userRepository.userPage(userPage);
    }

}
