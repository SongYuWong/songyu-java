package com.songyu.domains.auth.entity;

import com.mybatisflex.annotation.Table;
import com.songyu.commonutils.CommonStringUtils;
import com.songyu.components.springboot.data.AbstractTable;
import com.songyu.components.springboot.exception.IllegalInfoException;
import com.songyu.domains.auth.exception.IllegalResourceInfoException;
import com.songyu.domains.auth.valueObject.ResourceTypes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 资源
 * </p>
 *
 * @author songYu
 * @since 2023/9/15 16:19
 */
@Data
@Table("resource")
@EqualsAndHashCode(callSuper = true)
public class Resource extends AbstractTable {

    /**
     * 资源唯一号编码
     */
    private String resourceCode;

    /**
     * 父级资源编码
     */
    private String parentResourceCode;

    /**
     * 资源类型
     *
     * @see ResourceTypes ResourceTypes
     * @see ResourceTypes#name()
     */
    private String resourceType;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源描述
     */
    private String resourceDesc;

    @Override
    public void checkIfPrimaryInfoComplete() throws IllegalInfoException {
        if (CommonStringUtils.anyIsBlank(resourceName, resourceType)) {
            throw new IllegalResourceInfoException(this);
        }
    }

    public static boolean isComponentRes(Resource resource) {
        return ResourceTypes.COMPONENT.name().equals(resource.getResourceType());
    }

    public static boolean isRouterRes(Resource resource){
        return ResourceTypes.ROUTER.name().equals(resource.getResourceType());
    }

}
