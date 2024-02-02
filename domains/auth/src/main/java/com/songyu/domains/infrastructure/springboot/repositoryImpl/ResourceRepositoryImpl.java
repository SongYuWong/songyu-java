package com.songyu.domains.infrastructure.springboot.repositoryImpl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.songyu.commonutils.CommonCollectionUtils;
import com.songyu.domains.auth.aggregate.ResourcePage;
import com.songyu.domains.auth.entity.Resource;
import com.songyu.domains.auth.entity.ResourceRouter;
import com.songyu.domains.auth.entity.RoleResource;
import com.songyu.domains.auth.entity.UserRole;
import com.songyu.domains.auth.exception.IllegalResourceInfoException;
import com.songyu.domains.auth.repository.ResourceRepository;
import com.songyu.domains.infrastructure.mapper.ResourceMapper;
import com.songyu.domains.infrastructure.mapper.ResourceRouterMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 资源存储实现
 * </p>
 *
 * @author songYu
 * @since 2024/1/12 11:46
 */
@Component
public class ResourceRepositoryImpl extends ResourceRepository {

    @javax.annotation.Resource
    private ResourceMapper resourceMapper;

    @javax.annotation.Resource
    private ResourceRouterMapper resourceRouterMapper;

    @Override
    public List<Resource> listWithUserCode(String userCode) {
        return QueryChain.of(resourceMapper)
                .innerJoin(RoleResource.class)
                .on(queryWrapper -> queryWrapper.where(RoleResource::getResourceCode).eq(Resource::getResourceCode)
                        .innerJoin(UserRole.class)
                        .on(qw -> qw.where(UserRole::getUserCode).eq(userCode)
                                .and(UserRole::getRoleCode).eq(RoleResource::getRoleCode))).list();
    }

    @Override
    public Map<String, ResourceRouter> mappedByResourceCode(Set<String> resCodes) {
        return QueryChain.of(resourceRouterMapper)
                .where(ResourceRouter::getResourceCode)
                .in(resCodes, CommonCollectionUtils.isNotEmpty(resCodes))
                .list().stream().collect(Collectors.toMap(ResourceRouter::getResourceCode, Function.identity()));
    }

    @Override
    public void addNewResource(Resource resource) {

    }

    @Override
    public Resource getByResourceName(String resourceName) {
        return null;
    }

    @Override
    public Resource getByResourceCode(String resourceCode) {
        return null;
    }

    @Override
    public void disableResource(Resource resource) {

    }

    @Override
    public Resource getByResourceAnyUniqueInfo(Resource resource) throws IllegalResourceInfoException {
        return null;
    }

    @Override
    public void editResource(Resource resource) {

    }

    @Override
    public Page<ResourcePage> resourcePage(ResourcePage resourcePage) {
        return null;
    }
}
