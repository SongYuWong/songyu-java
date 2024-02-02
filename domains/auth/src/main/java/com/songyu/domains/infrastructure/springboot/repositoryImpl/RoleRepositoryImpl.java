package com.songyu.domains.infrastructure.springboot.repositoryImpl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.songyu.commonutils.CommonStringUtils;
import com.songyu.domains.auth.aggregate.RolePage;
import com.songyu.domains.auth.aggregate.UserPage;
import com.songyu.domains.auth.entity.Role;
import com.songyu.domains.auth.exception.IllegalRoleInfoException;
import com.songyu.domains.auth.repository.RoleRepository;
import com.songyu.domains.infrastructure.mapper.RoleMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>
 * 角色存储实现
 * </p>
 *
 * @author songYu
 * @since 2024/1/11 17:41
 */
@Component
public class RoleRepositoryImpl extends RoleRepository {

    @Resource
    RoleMapper roleMapper;

    @Override
    public Role getByRoleCode(String roleCode) {
        if (CommonStringUtils.isBlank(roleCode)) {
            throw new RuntimeException("缺少角色编码");
        }
        return QueryChain.of(roleMapper).where(Role::getRoleCode).eq(roleCode).one();
    }

    @Override
    public void addNewRole(Role role) {
        roleMapper.insert(role);
    }

    @Override
    public Role getByRoleName(String roleName) {
        if (CommonStringUtils.isBlank(roleName)) {
            throw new RuntimeException("缺少角色名称");
        }
        return QueryChain.of(roleMapper).where(Role::getRoleName).eq(roleName).one();
    }

    @Override
    public void disableRole(Role role) {
        if (CommonStringUtils.isBlank(role.getRoleCode())) {
            throw new RuntimeException("缺少角色编码");
        }
        roleMapper.deleteByQuery(QueryWrapper.create().where(Role::getRoleCode).eq(role.getRoleCode()));
    }

    @Override
    public Role getByRoleAnyUniqueInfo(Role role) throws IllegalRoleInfoException {
        if (CommonStringUtils.allIsBlank(role.getRoleCode(), role.getRoleName())) {
            throw new IllegalRoleInfoException(role);
        }
        return QueryChain.of(roleMapper)
                .where(Role::getRoleCode).eq(role.getRoleCode(), CommonStringUtils.isNotBlank(role.getRoleCode()))
                .or(Role::getRoleName).eq(role.getRoleName(), CommonStringUtils.isNotBlank(role.getRoleName())).one();
    }

    @Override
    public void editRole(Role role) {
        if (CommonStringUtils.isBlank(role.getRoleCode())) {
            throw new RuntimeException("缺少角色编码");
        }
        UpdateChain.create(roleMapper)
                .where(Role::getRoleCode).eq(role.getRoleCode())
                .set(Role::getRoleDesc, role.getRoleDesc())
                .set(Role::getRoleAlias, role.getRoleAlias())
                .update();
    }

    @Override
    public Page<RolePage> rolePage(RolePage rolePage) {
        return roleMapper.paginateAs(rolePage.getPage(), QueryWrapper.create(rolePage), RolePage.class);
    }

}
