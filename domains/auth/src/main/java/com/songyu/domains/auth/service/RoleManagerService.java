package com.songyu.domains.auth.service;

import com.mybatisflex.core.paginate.Page;
import com.songyu.components.springboot.exception.IllegalInfoException;
import com.songyu.domains.auth.aggregate.RolePage;
import com.songyu.domains.auth.entity.Role;
import com.songyu.domains.auth.exception.IllegalRoleInfoException;
import com.songyu.domains.auth.exception.RoleNotUniqueException;
import com.songyu.domains.auth.repository.RoleRepository;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * <p>
 * 角色管理相关业务
 * </p>
 *
 * @author songYu
 * @since 2024/1/11 15:46
 */
public abstract class RoleManagerService {

    @Resource
    private RoleRepository roleRepository;

    /**
     * 管理新增角色
     *
     * @param role 新增角色的角色信息
     * @return 新增的角色信息
     */
    public Role newRole(Role role) {
        try {
            checkRoleUnique(role);
        } catch (RoleNotUniqueException | IllegalInfoException e) {
            throw new RuntimeException(e);
        }
        roleRepository.addNewRole(role);
        return roleRepository.getByRoleName(role.getRoleName());
    }

    /**
     * 判断角色的唯一性
     *
     * @param role 角色信息
     * @throws RoleNotUniqueException   角色信息不唯一异常
     * @throws IllegalRoleInfoException 非法的角色信息异常
     */
    public void checkRoleUnique(Role role) throws RoleNotUniqueException, IllegalInfoException {
        role.checkIfPrimaryInfoComplete();
        Role selectedRole = roleRepository.getByRoleCode(role.getRoleCode());
        if (selectedRole != null) {
            if (role.getRoleName().equals(selectedRole.getRoleName())) {
                throw new RoleNotUniqueException("角色名已被使用", role);
            } else {
                throw new RoleNotUniqueException(role);
            }
        }
    }

    /**
     * 角色归档
     *
     * @param role 角色信息
     */
    public void archiveRole(Role role) {
        roleRepository.disableRole(role);
    }

    /**
     * 管理编辑角色
     *
     * @param role 角色信息
     * @return 更改后的角色信息
     */
    public Role editRole(Role role) {
        try {
            Role selectedRole = roleRepository.getByRoleAnyUniqueInfo(role);
            if (!Objects.equals(selectedRole.getRoleCode(), role.getRoleCode())) {
                throw new RuntimeException("角色名已被使用");
            } else {
                roleRepository.editRole(role);
                return roleRepository.getByRoleCode(role.getRoleCode());
            }
        } catch (IllegalRoleInfoException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据分页信息获取角色分页
     *
     * @param rolePage 分页信息
     * @return 角色分页数据
     */
    public Page<RolePage> rolePage(RolePage rolePage) {
        return roleRepository.rolePage(rolePage);
    }

}
