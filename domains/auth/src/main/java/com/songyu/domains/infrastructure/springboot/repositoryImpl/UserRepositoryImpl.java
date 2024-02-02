package com.songyu.domains.infrastructure.springboot.repositoryImpl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.songyu.commonutils.CommonStringUtils;
import com.songyu.domains.auth.aggregate.UserPage;
import com.songyu.domains.auth.entity.User;
import com.songyu.domains.auth.entity.UserClient;
import com.songyu.domains.auth.exception.IllegalUserInfoException;
import com.songyu.domains.auth.repository.UserRepository;
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
public class UserRepositoryImpl extends UserRepository {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserClientMapper userClientMapper;

    @Override
    public void addNewUser(User user) {
        if (user.getUserStatusCode() == null) {
            user.setUserStatusCode(UserStatus.REGISTERED.ordinal());
            user.setUserDesc("这个人什么都不想说。");
            user.setUserTel("");
            user.encryptInfo();
        }
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
    public void saveOrUpdateUserClient(UserClient userClient) {
        userClient.encryptInfo();
        UserClient one = QueryChain.of(userClientMapper)
                .where(UserClient::getUserCode).eq(userClient.getUserCode())
                .and(UserClient::getUserClientCode).eq(userClient.getUserClientCode())
                .one();
        if (one == null) {
            userClientMapper.insert(userClient);
        } else {
            UpdateChain.create(userClientMapper)
                    .where(UserClient::getUserCode).eq(userClient.getUserCode())
                    .and(UserClient::getUserClientCode).eq(userClient.getUserClientCode())
                    .set(UserClient::getUserClientRefreshToken, userClient.getUserClientRefreshToken(), CommonStringUtils.isNotBlank(userClient.getUserClientRefreshToken()));
        }
    }

    @Override
    public UserClient getUserClientByClientId(String clientId) {
        UserClient one = QueryChain.of(userClientMapper)
                .where(UserClient::getUserClientCode).eq(clientId)
                .one();
        if (one != null) {
            one.decryptInfo();
        }
        return one;
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

    @Override
    public User getUserByClientId(String clientId) {
        if (CommonStringUtils.isBlank(clientId)) {
            throw new RuntimeException("缺少客户端信息");
        }
        return QueryChain.of(userMapper).innerJoin(UserClient.class)
                .on(queryWrapper ->
                        queryWrapper
                                .where(UserClient::getUserClientCode).eq(clientId)
                                .and(User::getUserCode).eq(UserClient::getUserClientCode)
                ).one();
    }

    @Override
    public void clearUserClient(String clientId) {
        userClientMapper
                .deleteByQuery(new QueryWrapper().where(UserClient::getUserClientCode).eq(clientId));
    }

    @Override
    public Page<UserPage> userPage(UserPage userPage) {
        return userMapper.paginateAs(userPage.getPage(), QueryWrapper.create(), UserPage.class);
    }

    @Override
    public void editUser(User user) {
        if (CommonStringUtils.isBlank(user.getUserCode())) {
            throw new RuntimeException("缺少用户编码");
        }
        UpdateChain.create(userMapper)
                .where(User::getUserCode).eq(user.getUserCode())
                .set(User::getUserDesc, user.getUserDesc())
                .set(User::getUserTel, user.getUserTel())
                .update();
    }

    @Override
    public User getByUserCode(String userCode) {
        if (CommonStringUtils.isBlank(userCode)) {
            throw new RuntimeException("缺少用户编码");
        }
        return QueryChain.of(userMapper)
                .where(User::getUserCode).eq(userCode)
                .one();
    }

    @Override
    public void disableUser(User user) {
        if (CommonStringUtils.isBlank(user.getUserCode())) {
            throw new RuntimeException("缺少用户编码");
        }
        userClientMapper.deleteByQuery(QueryWrapper.create().where(UserClient::getUserCode).eq(user.getUserCode()));
        UpdateChain.create(userMapper)
                .where(User::getUserCode).eq(user.getUserCode())
                .set(User::getUserStatusCode, UserStatus.BLACK)
                .update();
    }

}
