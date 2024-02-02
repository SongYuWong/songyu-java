package com.songyu.domains.auth.repository;

import com.mybatisflex.core.paginate.Page;
import com.songyu.domains.auth.aggregate.RolePage;
import com.songyu.domains.auth.entity.Role;
import com.songyu.domains.auth.exception.IllegalRoleInfoException;

/**
 * <p>
 * 角色存储
 * </p>
 *
 * @author songYu
 * @since 2024/1/11 17:39
 */
public abstract class RoleRepository {

    /**
     * 根据角色编码获取角色信息
     *
     * @param roleCode 角色编码
     * @return 角色信息
     */
    public abstract Role getByRoleCode(String roleCode);

    /**
     * 添加新的角色
     *
     * @param role 角色信息
     */
    public abstract void addNewRole(Role role);

    /**
     * 根据角色名称获取角色信息
     *
     * @param roleName 角色名称
     * @return 角色信息
     */
    public abstract Role getByRoleName(String roleName);

    /**
     * 归档角色
     *
     * @param role 角色信息
     */
    public abstract void disableRole(Role role);

    /**
     * 根据角色唯一信息获取角色信息
     *
     * @param role 角色信息
     * @return 匹配的角色信息
     * @throws IllegalRoleInfoException 非法的角色信息异常
     */
    public abstract Role getByRoleAnyUniqueInfo(Role role) throws IllegalRoleInfoException;

    /**
     * 编辑角色信息
     * @param role 角色信息
     */
    public abstract void editRole(Role role);

    /**
     * 角色分页
     * @param rolePage 角色分页信息
     * @return 角色分页
     */
    public abstract Page<RolePage> rolePage(RolePage rolePage);

}
