package com.songyu.domains.auth.aggregate;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.core.paginate.Page;
import com.songyu.domains.auth.entity.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 资源分页
 * </p>
 *
 * @author songYu
 * @since 2024/1/11 16:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourcePage extends Resource {

    /**
     * 角色分页信息
     */
    @Column(ignore = true)
    private Page<ResourcePage> page;

}
