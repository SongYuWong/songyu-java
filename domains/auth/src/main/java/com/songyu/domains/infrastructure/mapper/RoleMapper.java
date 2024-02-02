package com.songyu.domains.infrastructure.mapper;

import com.mybatisflex.annotation.UseDataSource;
import com.mybatisflex.core.BaseMapper;
import com.songyu.domains.auth.entity.Role;

/**
 * <p>
 * 角色数据映射
 * </p>
 *
 * @author songYu
 * @since 2024/1/11 17:58
 */
@UseDataSource("SongYuAuth")
public interface RoleMapper extends BaseMapper<Role> {


}
