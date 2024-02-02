package com.songyu.domains.auth.entity;

import com.mybatisflex.annotation.Table;
import com.songyu.commonutils.CommonStringUtils;
import com.songyu.components.springboot.data.AbstractTable;
import com.songyu.components.springboot.exception.IllegalInfoException;
import com.songyu.domains.auth.exception.IllegalResourceInfoException;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 接口
 * </p>
 *
 * @author songYu
 * @since 2023/12/5 22:54
 */
@Data
@Table("api")
@EqualsAndHashCode(callSuper = true)
public class Api extends AbstractTable {

    /**
     * 接口编码
     */
    private String apiCode;

    /**
     * 接口地址
     */
    private String apiUri;

    /**
     * 接口方式
     */
    private String method;

    /**
     * 接口名称
     */
    private String apiName;

    /**
     * 接口描述
     */
    private String apiDesc;

    /**
     * 请求类型
     */
    private String requestType;

    /**
     * 响应类型
     */
    private String responseType;

    @Override
    public void checkIfPrimaryInfoComplete() throws IllegalInfoException {
        if (CommonStringUtils.anyIsBlank(method, apiUri, apiName)) {
            throw new IllegalResourceInfoException(this);
        }
    }
}
