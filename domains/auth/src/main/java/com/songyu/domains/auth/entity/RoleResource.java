package com.songyu.domains.auth.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * <p>
 * 角色资源
 * </p>
 *
 * @author songYu
 * @since 2023/12/5 22:58
 */
@Data
@Table("role_resource")
public class RoleResource {

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色编码
     */
    private String resourceCode;

}
