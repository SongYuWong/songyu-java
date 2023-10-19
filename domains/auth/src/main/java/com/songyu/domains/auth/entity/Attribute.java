package com.songyu.domains.auth.entity;

import com.mybatisflex.annotation.Table;
import com.songyu.components.springboot.data.AbstractTable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 权限属性
 * </p>
 *
 * @author songYu
 * @since 2023/9/15 11:09
 */
@Data
@Table("attribute")
@EqualsAndHashCode(callSuper = true)
public class Attribute extends AbstractTable {

    /**
     * 权限属性编码唯一号
     */
    private String attributeCode;

    /**
     * 权限属性名称
     */
    private String attributeName;

    /**
     * 权限属性别名
     */
    private String attributeAlias;

    /**
     * 权限属性描述
     */
    private String attributeDesc;

    /**
     * 父级权限属性唯一号编码
     */
    private String parentAttributeCode;

}
