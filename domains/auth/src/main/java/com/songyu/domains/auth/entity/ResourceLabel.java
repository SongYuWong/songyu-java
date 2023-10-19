package com.songyu.domains.auth.entity;

import com.mybatisflex.annotation.Table;
import com.songyu.components.springboot.data.AbstractTable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 资源标签
 * </p>
 *
 * @author songYu
 * @since 2023/9/15 16:19
 */
@Data
@Table("resource_label")
@EqualsAndHashCode(callSuper = true)
public class ResourceLabel extends AbstractTable {

    /**
     * 资源标签唯一号编码
     */
    private String resourceLabelCode;

    /**
     * 资源标签名称
     */
    private String resourceLabelName;

    /**
     * 资源标签描述
     */
    private String resourceLabelDesc;

}
