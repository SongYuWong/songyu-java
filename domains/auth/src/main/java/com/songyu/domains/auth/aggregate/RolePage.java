package com.songyu.domains.auth.aggregate;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.core.paginate.Page;
import com.songyu.domains.auth.entity.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色分页
 * </p>
 *
 * @author songYu
 * @since 2024/1/11 16:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RolePage extends Role {

    /**
     * 角色分页信息
     */
    @Column(ignore = true)
    private Page<RolePage> page;

}
