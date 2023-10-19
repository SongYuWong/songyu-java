package com.songyu.domains.infrastructure.mapper;

import com.mybatisflex.annotation.UseDataSource;
import com.songyu.components.springboot.data.AbstractRepository;
import com.songyu.domains.auth.entity.User;

/**
 * <p>
 * 用户数据库映射
 * </p>
 *
 * @author songYu
 * @since 2023/9/20 11:04
 */
@UseDataSource("SongYuAuth")
public interface UserMapper extends AbstractRepository<User> {



}
