package com.songyu.domains.auth.entity;

import com.mybatisflex.annotation.Table;
import com.songyu.components.springboot.data.AbstractTable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户客户端
 * </p>
 *
 * @author songYu
 * @since 2023/9/15 10:58
 */
@Data
@Table("user_client")
@EqualsAndHashCode(callSuper = true)
public class UserClient extends AbstractTable {

    /**
     * 用户客户端编码唯一号
     */
    private String userClientCode;

    /**
     * 用户客户端公钥
     */
    private String userClientKey;

    /**
     * 用户客户端指纹
     */
    private String userClientFingerprint;

    /**
     * 用户编码唯一号
     */
    private String userCode;

}
