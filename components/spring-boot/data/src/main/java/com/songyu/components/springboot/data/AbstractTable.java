package com.songyu.components.springboot.data;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 表的基础字段封装
 * </p>
 *
 * @author songYu
 * @since 2023/9/15 11:13
 */
@Data
public abstract class AbstractTable {

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

}
