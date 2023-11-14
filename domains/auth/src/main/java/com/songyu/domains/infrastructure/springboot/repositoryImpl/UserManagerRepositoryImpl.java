package com.songyu.domains.infrastructure.springboot.repositoryImpl;

import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.songyu.commonutils.CommonStringUtils;
import com.songyu.domains.auth.entity.User;
import com.songyu.domains.auth.entity.UserClient;
import com.songyu.domains.auth.exception.IllegalUserInfoException;
import com.songyu.domains.auth.repository.UserManagerRepository;
import com.songyu.domains.auth.valueObject.UserStatus;
import com.songyu.domains.infrastructure.mapper.UserClientMapper;
import com.songyu.domains.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>
 * 用户管理存储 mapper 实现
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 9:44
 */
@Component
public class UserManagerRepositoryImpl extends UserManagerRepository {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserClientMapper userClientMapper;

    @Override
    public void addNewUser(User user) {
        user.setUserStatusCode(UserStatus.REGISTERED.ordinal());
        user.setUserDesc("这个人什么都不想说。");
        user.setUserTel("");
        userMapper.insert(user);
    }

    @Override
    public void activateUser(User user) {
        if (CommonStringUtils.isBlank(user.getUserEmail())) {
            throw new RuntimeException("无效的激活用户邮箱");
        }
        UpdateChain.of(User.class)
                .set(User::getUserStatusCode, UserStatus.NORMAL.ordinal())
                .where(User::getUserEmail).eq(user.getUserEmail())
                .and(User::getUserStatusCode).eq(UserStatus.REGISTERED.ordinal())
                .update();
    }

    @Override
    public User getByUserName(String userName) {
        if (CommonStringUtils.isBlank(userName)) {
            throw new RuntimeException("缺少用户名");
        }
        return QueryChain.of(userMapper)
                .where(User::getUserName).eq(userName)
                .one();
    }

    @Override
    public User getByUserEmail(String userEmail) {
        if (CommonStringUtils.isBlank(userEmail)) {
            throw new RuntimeException("缺少用户邮箱");
        }
        return QueryChain.of(userMapper)
                .where(User::getUserEmail).eq(userEmail)
                .one();
    }

    @Override
    public User getByUserAnyUniqueInfo(User user) throws IllegalUserInfoException {
        if (CommonStringUtils.allIsBlank(user.getUserCode(), user.getUserName(), user.getUserEmail())) {
            throw new IllegalUserInfoException(user);
        }
        return QueryChain.of(userMapper)
                .where(User::getUserCode).eq(user.getUserCode(), CommonStringUtils.isNotBlank(user.getUserCode()))
                .or(User::getUserEmail).eq(user.getUserEmail(), CommonStringUtils.isNotBlank(user.getUserEmail()))
                .or(User::getUserName).eq(user.getUserName(), CommonStringUtils.isNotBlank(user.getUserName())).one();
    }

    @Override
    public User getByUserUniqueInfo(User user) throws IllegalUserInfoException {
        if (CommonStringUtils.allIsBlank(user.getUserCode(), user.getUserName(), user.getUserEmail())) {
            throw new IllegalUserInfoException(user);
        }
        QueryChain<User> queryChain = QueryChain.of(userMapper)
                .where(User::getUserCode).eq(user.getUserCode(), CommonStringUtils.isNotBlank(user.getUserCode()));
        if (CommonStringUtils.anyIsBlank(user.getUserEmail(), user.getUserName())) {
            queryChain
                    .or(User::getUserEmail).eq(user.getUserEmail(), CommonStringUtils.isNotBlank(user.getUserEmail()))
                    .or(User::getUserName).eq(user.getUserName(), CommonStringUtils.isNotBlank(user.getUserName()));
        } else {
            queryChain.or(queryWrapper -> {
                queryWrapper.where(User::getUserEmail).eq(user.getUserEmail(), CommonStringUtils.isNotBlank(user.getUserEmail()))
                        .and(User::getUserName).eq(user.getUserName(), CommonStringUtils.isNotBlank(user.getUserName()));
            });
        }
        return queryChain.one();
    }

    @Override
    public void newUserClient(UserClient userClient) {
        userClientMapper.insert(userClient);
    }

    @Override
    public UserClient getUserClientByClientId(String clientId) {
        return QueryChain.of(userClientMapper)
                .where(UserClient::getUserClientCode).eq(clientId)
                .one();
    }

    @Override
    public User getByUserNameOrEmail(String userNameOrEmail) {
        if (CommonStringUtils.isBlank(userNameOrEmail)) {
            throw new RuntimeException("缺少用户账户信息");
        }
        return QueryChain.of(userMapper)
                .where(User::getUserEmail).eq(userNameOrEmail)
                .or(User::getUserName).eq(userNameOrEmail)
                .one();
    }

}
