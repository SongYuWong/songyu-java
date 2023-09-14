package com.songyu.commonutils;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

class CommonDateUtilsTest {

    @Test
    void test() {
        Date date = new Date();
        System.out.println(CommonDateUtils.toString(date));
        System.out.println(CommonDateUtils.toString(CommonDateUtils.addHours(date, 2)));
        System.out.println(CommonDateUtils.toString(CommonDateUtils.addHours(date, -2)));
        System.out.println(CommonDateUtils.toString(CommonDateUtils.addTimes(date, -2, TimeUnit.DAYS)));
    }

}