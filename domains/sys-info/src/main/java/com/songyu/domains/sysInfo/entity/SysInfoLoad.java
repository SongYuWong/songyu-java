package com.songyu.domains.sysInfo.entity;

import lombok.Data;

/**
 * <p>
 * 系统已使用资源信息
 * </p>
 *
 * @author songYu
 * @since 2024/1/7 22:52
 */
@Data
public class SysInfoLoad {

    /**
     * CPU 使用率%
     */
    private Double cpuLoad;

    /**
     * 内存使用大小
     */
    private Long memoryUsed;

    /**
     * 内存空闲大小
     */
    private Long memoryFree;

    /**
     * 程序启动时间
     */
    private Long startTime;

}
