package com.songyu.commonutils;

import org.junit.jupiter.api.Test;

class CommonSysInfoUtilsTest {

    @Test
    void getSysMemoryLoad() {
        System.out.println("Memory " + CommonSysInfoUtils.getSysMemoryLoad());
    }

    @Test
    void getSysCpuLoad() {
        System.out.println("CPU " + CommonSysInfoUtils.getSysCpuLoad());
    }
}