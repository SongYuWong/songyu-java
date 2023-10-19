package com.songyu.domains.infrastructure.springboot.repositoryImpl;

import com.songyu.domains.auth.service.ResourceManagerService;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * <p>
 * 属性资源管理业务
 * </p>
 *
 * @author songYu
 * @since 2023/9/25 14:57
 */
@Component
public class ResourceManagerServiceImpl extends ResourceManagerService {

    @Override
    public Set<String> getUserResourceLabels(String userCode) {
        return null;
    }

}
