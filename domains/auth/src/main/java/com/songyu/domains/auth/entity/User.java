package com.songyu.domains.auth.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.songyu.commonutils.CommonAESEncryptUtils;
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
     * 用户客户端配置 json
     */
    private String userClientConfig;

    /**
     * 状态码是否是注册
     *
     * @param userStatusCode 状态码
     * @return 是否是注册
     */
    public static boolean statusRegistering(int userStatusCode) {
        return UserStatus.REGISTERED.ordinal() == userStatusCode;
    }

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
            throw new IllegalUserStatusException(userStatusCode);
        }
    }

    /**
     * 校验用户登录信息
     *
     * @param user 用户登录信息
     */
    public void checkLoginInfo(User user) {
        if (this.userPassword.equals(user.getUserPassword())) {
            if (CommonStringUtils.isNotBlank(user.getUserCode())) {
                if (user.getUserCode().equals(this.userName) ||
                        user.getUserCode().equals(this.userEmail)) {
                    return;
                }
            }
        }
        throw new RuntimeException("用户名/邮箱或密码错误！");
    }

    /**
     * 加密用户的信息用于持久化
     */
    public void encryptInfo() {
        this.userPassword = CommonAESEncryptUtils.encryptObject(this.userPassword, "abDn4R1LwavicEuNFAO");
        this.userEmail = CommonAESEncryptUtils.encryptObject(this.userEmail, "abDn4R1LwAJG6aheSEuNFAO");
        this.userTel = CommonAESEncryptUtils.encryptObject(this.userTel, "abDn4R1LwAJG6aNFAO");
        this.userName = CommonAESEncryptUtils.encryptObject(this.userName, "abDn4R1LwAJG6aheSEE1p2O");
    }

    /**
     * 解密用户持久化的信息
     */
    public void decryptInfo() {
        this.userPassword = CommonAESEncryptUtils.decryptObject(this.userPassword, "abDn4R1LwavicEuNFAO", String.class);
        this.userEmail = CommonAESEncryptUtils.decryptObject(this.userEmail, "abDn4R1LwAJG6aheSEuNFAO", String.class);
        this.userTel = CommonAESEncryptUtils.decryptObject(this.userTel, "abDn4R1LwAJG6aNFAO", String.class);
        this.userName = CommonAESEncryptUtils.decryptObject(this.userName, "abDn4R1LwAJG6aheSEE1p2O", String.class);
    }

    public String toInsertSql() {
        return "insert into auth.user \n(user_code, user_name, user_desc, user_email, user_tel, user_password, user_status_code) values \n(" +
                "'" + userCode + "', " +
                "'" + userName + "', " +
                "'" + userDesc + "', " +
                "'" + userEmail + "', " +
                "'" + userTel + "', " +
                "'" + userPassword + "', " +
                userStatusCode +
                ");";
    }

    public void clearTokenExcepted() {
        this.setUserCode(null);
        this.setUserPassword(null);
        this.setUserTel(null);
        this.setUserDesc(null);
        this.setUserEmail(null);
        this.setUserStatusCode(null);
    }
}
