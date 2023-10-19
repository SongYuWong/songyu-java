package com.songyu.domains.infrastructure.mapper;

import com.mybatisflex.annotation.UseDataSource;
import com.mybatisflex.core.BaseMapper;
import com.songyu.domains.auth.entity.UserClient;

/**
 * <p>
 * 用户客户端数据映射
 * </p>
 *
 * @author songYu
 * @since 2023/9/25 15:59
 */
@UseDataSource("SongYuAuth")
public interface UserClientMapper extends BaseMapper<UserClient> {


}
