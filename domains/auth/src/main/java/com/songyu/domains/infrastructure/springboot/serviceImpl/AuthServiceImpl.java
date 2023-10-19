package com.songyu.domains.infrastructure.springboot.serviceImpl;

import com.songyu.components.cache.CacheService;
import com.songyu.components.lock.LockService;
import com.songyu.components.springboot.email.EmailSendService;
import com.songyu.domains.auth.service.ResourceManagerService;
import com.songyu.domains.auth.service.AuthService;
import com.songyu.domains.auth.service.UserManagerService;
import com.songyu.domains.infrastructure.springboot.config.props.SyAuth;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户管理服务 spring boot 实现
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 10:59
 */
@Service
public class AuthServiceImpl extends AuthService {

    public AuthServiceImpl(UserManagerService userManagerService,
                           EmailSendService emailSendService,
                           CacheService cacheService,
                           ResourceManagerService resourceManagerService,
                           SyAuth syAuth,
                           LockService<?> lockService) {
        super(userManagerService, emailSendService, cacheService, resourceManagerService, syAuth, lockService);
    }

}
