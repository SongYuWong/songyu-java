package com.songyu.domains.sysInfo.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SysInfoLoadServiceTest {

    @Test
    void getSysInfoLoad() {
        SysInfoLoadService sysInfoLoadService = new SysInfoLoadService();
        System.out.println(sysInfoLoadService.getSysInfoLoad());
    }
}