package com.songyu.domains.auth.entity;

import com.mybatisflex.annotation.Table;
import com.songyu.commonutils.CommonStringUtils;
import com.songyu.components.springboot.data.AbstractTable;
import com.songyu.components.springboot.exception.IllegalInfoException;
import com.songyu.domains.auth.exception.IllegalRoleInfoException;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author songYu
 * @since 2023/9/15 11:09
 */
@Data
@Table("role")
@EqualsAndHashCode(callSuper = true)
public class Role extends AbstractTable {

    /**
     * 角色编码唯一号
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色别名
     */
    private String roleAlias;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 父级角色唯一号编码
     */
    private String parentRoleCode;

    public void checkIfPrimaryInfoComplete() throws IllegalInfoException {
        if (CommonStringUtils.anyIsBlank(roleName)) {
            throw new IllegalRoleInfoException(this);
        }
    }
}
