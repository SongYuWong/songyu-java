package com.songyu.domains.auth.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * <p>
 * 资源接口
 * </p>
 *
 * @author songYu
 * @since 2023/12/5 23:00
 */
@Data
@Table("resource_api")
public class ResourceApi {

    /**
     * 资源编码
     */
    private String resourceCode;

    /**
     * 接口编码
     */
    private String apiCode;

}
