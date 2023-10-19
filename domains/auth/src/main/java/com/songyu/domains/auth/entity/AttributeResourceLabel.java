package com.songyu.domains.auth.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * <p>
 * 权限属性资源标签
 * </p>
 *
 * @author songYu
 * @since 2023/9/15 16:30
 */
@Data
@Table("attribute_resource_label")
public class AttributeResourceLabel {

    /**
     * 权限属性标签
     */
    private String attributeCode;

    /**
     * 资源标签编码
     */
    private String resourceLabelCode;

}
