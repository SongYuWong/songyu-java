package com.songyu.domains.auth.aggregate;

import com.songyu.commonutils.CommonStringUtils;
import com.songyu.domains.auth.entity.User;
import lombok.Data;

/**
 * <p>
 * 用户注册聚合
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 16:41
 */
@Data
public class UserRegistered {

    /**
     * 用户信息聚合根
     */
    private User user;

    /**
     * 用户激活码
     */
    private String activeCode;

    /**
     * 激活码是否有效
     *
     * @param activeCode 激活码
     * @return 是否有效
     */
    public boolean ifActiveCodeValid(String activeCode) {
        if (CommonStringUtils.isBlank(activeCode)) {
            return false;
        }
        return activeCode.equals(this.activeCode);
    }

}
