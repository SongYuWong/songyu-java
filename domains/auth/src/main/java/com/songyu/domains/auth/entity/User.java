package com.songyu.domains.auth.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.songyu.commonutils.CommonStringUtils;
import com.songyu.components.springboot.data.AbstractTable;
import com.songyu.domains.auth.exception.IllegalUserInfoException;
import com.songyu.domains.auth.exception.IllegalUserStatusException;
import com.songyu.domains.auth.valueObject.UserStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author songYu
 * @since 2023/9/15 10:58
 */
@Data
@Table("user")
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractTable {

    /**
     * 用户编码唯一号
     */
    @Id(keyType = KeyType.Generator, value = "nanoid")
    private String userCode;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户简介
     */
    private String userDesc;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户电话号码
     */
    private String userTel;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户状态码
     */
    private Integer userStatusCode;

    /**
     * 校验用户主要信息是否完整
     */
    public void checkIfPrimaryInfoComplete() throws IllegalUserInfoException {
        if (CommonStringUtils.anyIsBlank(userName, userEmail, userPassword)) {
            throw new IllegalUserInfoException(this);
        }
    }

    /**
     * 判断用户状态是否有效
     *
     * @throws IllegalUserStatusException 用户状态无效异常
     */
    public void ifUserStatusValid() throws IllegalUserStatusException {
        if (userStatusCode == null
                || userStatusCode.equals(UserStatus.REGISTERED.ordinal())
                || userStatusCode.equals(UserStatus.BLACK.ordinal())) {
            throw new IllegalUserStatusException(UserStatus.mathByCode(userStatusCode));
        }
    }

    /**
     * 校验用户登录信息
     *
     * @param user 用户登录信息
     */
    public void checkLoginInfo(User user) {
        if (this.userPassword.equals(user.getUserPassword())) {
            if (CommonStringUtils.isNotBlank(user.getUserName())) {
                if (user.getUserName().equals(this.userName)) {
                    return;
                }
            }
            if (CommonStringUtils.isNotBlank(user.getUserEmail())) {
                if (user.getUserEmail().equals(this.userEmail)) {
                    return;
                }
            }
        }
        throw new RuntimeException("用户登录信息错误！");
    }

}
