package com.songyu.domains.auth.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * <p>
 * 用户的权限属性
 * </p>
 *
 * @author songYu
 * @since 2023/9/15 16:15
 */
@Data
@Table("user_attribute")
public class UserAttribute {

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 权限属性编码
     */
    private String attributeCode;

}
