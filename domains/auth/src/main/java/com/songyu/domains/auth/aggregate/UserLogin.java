package com.songyu.domains.auth.aggregate;

import com.songyu.domains.auth.entity.User;
import lombok.Data;

/**
 * <p>
 * 用户登录信息聚合
 * </p>
 *
 * @author songYu
 * @since 2023/9/22 8:51
 */
@Data
public class UserLogin {

    /**
     * 用户信息聚合根
     */
    private User user;

    /**
     * 客户端唯一号
     */
    private String clientId;

    /**
     * 客户端指纹信息
     */
    private String clientFingerprint;

    /**
     * 客户端公钥
     */
    private String clientKey;

}
