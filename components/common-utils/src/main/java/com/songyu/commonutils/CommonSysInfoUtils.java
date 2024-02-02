package com.songyu.commonutils;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.Util;

/**
 * <p>
 * 通用系统信息工具
 * </p>
 *
 * @author songYu
 * @since 2024/1/7 15:18
 */
public class CommonSysInfoUtils {

    private static final SystemInfo systemInfo = new SystemInfo();

    /**
     * 获取系统内存使用率
     *
     * @return 系统内存使用率%
     */
    public static double getSysMemoryLoad() {
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        GlobalMemory memory = hardware.getMemory();
        return (1 - ((double) memory.getAvailable() / (double) memory.getTotal())) * 100;
    }

    /**
     * 获取空闲的内存
     *
     * @return 空闲内存大小
     */
    public static long getSysMemoryFree() {
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        GlobalMemory memory = hardware.getMemory();
        return memory.getAvailable();
    }

    /**
     * 获取总的内存
     *
     * @return 总内存大小
     */
    public static long getSysMemoryTotal() {
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        GlobalMemory memory = hardware.getMemory();
        return memory.getTotal();
    }

    /**
     * 获取系统 CPU 使用率
     *
     * @return 系统 CPU 使用率%
     */
    public static double getSysCpuLoad() {
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        double result;
        int i = 0;
        while (true) {
            result = getSysCpuLoadInner(processor, ++i * 10);
            if (result > 0) {
                return result;
            }
        }
    }

    private static double getSysCpuLoadInner(CentralProcessor processor, int ms) {
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(ms);
        return processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
    }

}
