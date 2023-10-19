package com.songyu.domains.infrastructure.springboot.serviceImpl;

import com.songyu.domains.auth.repository.UserManagerRepository;
import com.songyu.domains.auth.service.UserManagerService;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 用户管理相关业务实现
 * </p>
 *
 * @author songYu
 * @since 2023/9/25 16:35
 */
@Component
public class UserManagerServiceImpl extends UserManagerService {

    public UserManagerServiceImpl(UserManagerRepository userManagerRepository) {
        super(userManagerRepository);
    }


}
