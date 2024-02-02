package com.songyu.domains.auth.service;

import com.mybatisflex.core.paginate.Page;
import com.songyu.commonutils.CommonCollectionUtils;
import com.songyu.components.springboot.exception.IllegalInfoException;
import com.songyu.domains.auth.aggregate.AuthClient;
import com.songyu.domains.auth.aggregate.ResourcePage;
import com.songyu.domains.auth.entity.*;
import com.songyu.domains.auth.exception.IllegalResourceInfoException;
import com.songyu.domains.auth.exception.ResourceNotUniqueException;
import com.songyu.domains.auth.repository.ResourceRepository;
import com.songyu.domains.auth.valueObject.ResourceTypes;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>
 * 属性资源管理业务
 * </p>
 *
 * @author songYu
 * @since 2023/9/20 18:43
 */
public abstract class ResourceManagerService {

    @javax.annotation.Resource
    private ResourceRepository resourceRepository;

    /**
     * 设置认证客户端所拥有的资源
     *
     * @param authClient 认证客户端
     */
    public void authUserResources(AuthClient authClient) {
        String userCode = authClient.getUser().getUserCode();
        List<Resource> resources = resourceRepository.listWithUserCode(userCode);
        Set<String> routerResCodes = resources.stream().filter(Resource::isRouterRes).map(Resource::getResourceCode).collect(Collectors.toSet());
        if (CommonCollectionUtils.isEmpty(routerResCodes)) {
            throw new RuntimeException("用户无界面访问权限，请分配后使用");
        }
        Map<String, ResourceRouter> resourceRouterMap = resourceRepository.mappedByResourceCode(routerResCodes);
        for (Resource resource : resources) {
            authClient.addComponentCodes(resource);
            if (Resource.isRouterRes(resource)) {
                authClient.addResourceRouter(resource, resourceRouterMap);
            }
        }
    }
    
    /**
     * 管理新增资源
     *
     * @param resource 新增资源的资源信息
     * @return 新增的资源信息
     */
    public Resource newResource(Resource resource) {
        try {
            checkResourceUnique(resource);
        } catch (ResourceNotUniqueException | IllegalInfoException e) {
            throw new RuntimeException(e);
        }
        resourceRepository.addNewResource(resource);
        return resourceRepository.getByResourceName(resource.getResourceName());
    }

    /**
     * 判断资源的唯一性
     *
     * @param resource 资源信息
     * @throws ResourceNotUniqueException   资源信息不唯一异常
     * @throws IllegalResourceInfoException 非法的资源信息异常
     */
    public void checkResourceUnique(Resource resource) throws ResourceNotUniqueException, IllegalInfoException {
        resource.checkIfPrimaryInfoComplete();
        Resource selectedResource = resourceRepository.getByResourceCode(resource.getResourceCode());
        if (selectedResource != null) {
            if (resource.getResourceName().equals(selectedResource.getResourceName())) {
                throw new ResourceNotUniqueException("资源名已被使用", resource);
            } else {
                throw new ResourceNotUniqueException(resource);
            }
        }
    }

    /**
     * 资源归档
     *
     * @param resource 资源信息
     */
    public void archiveResource(Resource resource) {
        resourceRepository.disableResource(resource);
    }

    /**
     * 管理编辑资源
     *
     * @param resource 资源信息
     * @return 更改后的资源信息
     */
    public Resource editResource(Resource resource) {
        try {
            Resource selectedResource = resourceRepository.getByResourceAnyUniqueInfo(resource);
            if (!Objects.equals(selectedResource.getResourceCode(), resource.getResourceCode())) {
                throw new RuntimeException("资源名已被使用");
            } else {
                resourceRepository.editResource(resource);
                return resourceRepository.getByResourceCode(resource.getResourceCode());
            }
        } catch (IllegalResourceInfoException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据分页信息获取资源分页
     *
     * @param resourcePage 分页信息
     * @return 资源分页数据
     */
    public Page<ResourcePage> resourcePage(ResourcePage resourcePage) {
        return resourceRepository.resourcePage(resourcePage);
    }

}
