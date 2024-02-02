package com.songyu.domains.auth.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * <p>
 * 路由资源
 * </p>
 *
 * @author songYu
 * @since 2023/12/5 23:03
 */
@Data
@Table("resource_router")
public class ResourceRouter {

    /**
     * 资源编码
     */
    private String resourceCode;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 路由名称
     */
    private String name;

    /**
     * 组件名
     */
    private String component;

    /**
     * 路由元数据
     */
    private String meta;

}
