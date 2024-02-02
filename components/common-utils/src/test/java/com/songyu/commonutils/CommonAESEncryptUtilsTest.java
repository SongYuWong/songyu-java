package com.songyu.commonutils;

import org.junit.jupiter.api.Test;

import java.util.Date;

class CommonAESEncryptUtilsTest {

    @Test
    void testStr(){
        String origin = "你好啊，银河!adsgshgdfsgafgdshfjgk";
        String s = CommonAESEncryptUtils.encryptString(origin);
        System.out.println(s);
        String s1 = CommonAESEncryptUtils.decryptString(s);
        System.out.println(s1);
        String s2 = CommonAESEncryptUtils.encryptString(origin,"212");
        System.out.println(s2);
        String s3 = CommonAESEncryptUtils.decryptString(s2,"212");
        System.out.println(s3);
    }

    @Test
    void testObj(){
        Date o = new Date(1251514561651L);
        String s = CommonAESEncryptUtils.encryptObject(o);
        System.out.println(s);
        Date s1 = CommonAESEncryptUtils.decryptObject(s, Date.class);
        System.out.println(s1);
        String s2 = CommonAESEncryptUtils.encryptObject(o,"212");
        System.out.println(s2);
        Date s3 = CommonAESEncryptUtils.decryptObject(s2,"212", Date.class);
        System.out.println(s3);
    }

}