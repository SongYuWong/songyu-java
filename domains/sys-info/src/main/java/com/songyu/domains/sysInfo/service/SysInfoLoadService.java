package com.songyu.domains.sysInfo.service;

import com.songyu.commonutils.CommonSysInfoUtils;
import com.songyu.domains.sysInfo.entity.SysInfoLoad;

/**
 * <p>
 * 系统资源使用情况业务
 * </p>
 *
 * @author songYu
 * @since 2024/1/7 23:03
 */
public class SysInfoLoadService {

    /**
     * 静态变量记录开始时间
     */
    private static final long startTime = System.currentTimeMillis();

    /**
     * 获取系统占用资源信息
     *
     * @return 系统占用资源信息
     */
    public SysInfoLoad getSysInfoLoad() {
        SysInfoLoad sysInfoLoad = new SysInfoLoad();
        long sysMemoryFree = CommonSysInfoUtils.getSysMemoryFree();
        sysInfoLoad.setMemoryFree(sysMemoryFree);
        sysInfoLoad.setMemoryUsed(CommonSysInfoUtils.getSysMemoryTotal() - sysMemoryFree);
        sysInfoLoad.setCpuLoad(CommonSysInfoUtils.getSysCpuLoad());
        sysInfoLoad.setStartTime(startTime);
        return sysInfoLoad;
    }

}
