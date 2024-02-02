package com.songyu.apps.web.controller;

import com.songyu.components.api.ApiDoc;
import com.songyu.domains.sysInfo.entity.SysInfoLoad;
import com.songyu.domains.sysInfo.service.SysInfoLoadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 系统信息接口
 * </p>
 *
 * @author songYu
 * @since 2024/1/7 22:46
 */
@RestController
@RequestMapping("/sysInfo")
public class SysInfoController {

    @Resource
    private SysInfoLoadService sysInfoLoadService;

    @PostMapping("/loaded")
    @ApiDoc(name = "系统资源使用情况", desc = "系统资源使用情况", requestType = Void.class, responseType = SysInfoLoad.class)
    public SysInfoLoad loaded() {
        return sysInfoLoadService.getSysInfoLoad();
    }

}
