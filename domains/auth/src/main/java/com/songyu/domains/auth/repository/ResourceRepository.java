package com.songyu.domains.auth.repository;

import com.mybatisflex.core.paginate.Page;
import com.songyu.domains.auth.aggregate.ResourcePage;
import com.songyu.domains.auth.entity.Resource;
import com.songyu.domains.auth.entity.ResourceRouter;
import com.songyu.domains.auth.exception.IllegalResourceInfoException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 资源存储
 * </p>
 *
 * @author songYu
 * @since 2024/1/12 11:46
 */
public abstract class ResourceRepository {

    /**
     * 根据用户编码获取资源列表
     *
     * @param userCode 用户编码
     * @return 资源列表
     */
    public abstract List<Resource> listWithUserCode(String userCode);

    /**
     * 通过资源编码集合获取资源编码映射资源映射
     *
     * @param resCodes 资源编码集合
     * @return 资源编码映射资源映射
     */
    public abstract Map<String, ResourceRouter> mappedByResourceCode(Set<String> resCodes);

    /**
     * 添加新的资源
     *
     * @param resource 资源信息
     */
    public abstract void addNewResource(Resource resource);

    /**
     * 根据资源名称获取资源信息
     *
     * @param resourceName 资源名称
     * @return 资源信息
     */
    public abstract Resource getByResourceName(String resourceName);

    /**
     * 根据资源编码获取资源信息
     *
     * @param resourceCode 资源编码
     * @return 资源信息
     */
    public abstract Resource getByResourceCode(String resourceCode);

    /**
     * 归档资源
     *
     * @param resource 资源信息
     */
    public abstract void disableResource(Resource resource);


    /**
     * 根据资源唯一信息获取资源信息
     *
     * @param resource 资源信息
     * @return 匹配的资源信息
     * @throws IllegalResourceInfoException 非法的资源信息异常
     */
    public abstract Resource getByResourceAnyUniqueInfo(Resource resource) throws IllegalResourceInfoException;

    /**
     * 编辑资源信息
     *
     * @param resource 资源信息
     */
    public abstract void editResource(Resource resource);

    /**
     * 资源分页
     *
     * @param resourcePage 资源分页信息
     * @return 资源分页
     */
    public abstract Page<ResourcePage> resourcePage(ResourcePage resourcePage);
}
