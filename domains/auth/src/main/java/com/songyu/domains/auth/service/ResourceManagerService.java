package com.songyu.domains.auth.service;

import java.util.Set;

/**
 * <p>
 * 属性资源管理业务
 * </p>
 *
 * @author songYu
 * @since 2023/9/20 18:43
 */
public abstract class ResourceManagerService {

    /**
     * 获取用户资源标签集合
     *
     * @param userCode 用户唯一号编码
     * @return 用户资源标签集合
     */
    public abstract Set<String> getUserResourceLabels(String userCode);

}
