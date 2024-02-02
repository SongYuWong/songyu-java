package com.songyu.domains.infrastructure.mapper;

import com.mybatisflex.annotation.UseDataSource;
import com.songyu.components.springboot.data.AbstractRepository;
import com.songyu.domains.auth.entity.ResourceRouter;

/**
 * <p>
 * 路由资源数据库映射
 * </p>
 *
 * @author songYu
 * @since 2023/12/7 10:07
 */
@UseDataSource("SongYuAuth")
public interface ResourceRouterMapper extends AbstractRepository<ResourceRouter> {


}
