package com.songyu.domains.auth.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * <p>
 * 用户的角色
 * </p>
 *
 * @author songYu
 * @since 2023/9/15 16:15
 */
@Data
@Table("user_role")
public class UserRole {

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 角色编码
     */
    private String roleCode;

}
