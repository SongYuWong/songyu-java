package com.songyu.apps.web;

import com.songyu.components.springboot.mvc.service.EndpointsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@SpringBootTest
class WebApplicationTest {

    @Autowired
    Environment environment;

    @Resource
    EndpointsService endpointsService;

    @Test
    void Endpoints() {
        endpointsService.getApiInfos().values().forEach(System.out::println);
    }

}