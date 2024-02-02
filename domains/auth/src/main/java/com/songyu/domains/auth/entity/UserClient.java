package com.songyu.domains.auth.entity;

import com.mybatisflex.annotation.Table;
import com.songyu.commonutils.CommonAESEncryptUtils;
import com.songyu.commonutils.CommonStringUtils;
import com.songyu.components.springboot.data.AbstractTable;
import com.songyu.components.springboot.exception.IllegalInfoException;
import com.songyu.domains.auth.exception.IllegalUserClientInfoException;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户客户端
 * </p>
 *
 * @author songYu
 * @since 2023/9/15 10:58
 */
@Data
@Table("user_client")
@EqualsAndHashCode(callSuper = true)
public class UserClient extends AbstractTable {

    /**
     * 用户客户端编码唯一号
     */
    private String userClientCode;

    /**
     * 用户客户端公钥
     */
    private String userClientRefreshToken;

    /**
     * 用户编码唯一号
     */
    private String userCode;

    @Override
    public void encryptInfo() {
        this.userClientRefreshToken = CommonAESEncryptUtils.encryptObject(this.userClientRefreshToken);
    }

    @Override
    public void decryptInfo() {
        this.userClientRefreshToken = CommonAESEncryptUtils.decryptObject(this.userClientRefreshToken, String.class);
    }

    @Override
    public void checkIfPrimaryInfoComplete() throws IllegalInfoException {
        if (CommonStringUtils.anyIsBlank(userCode, userClientRefreshToken)) {
            throw new IllegalUserClientInfoException(this);
        }
    }

}
