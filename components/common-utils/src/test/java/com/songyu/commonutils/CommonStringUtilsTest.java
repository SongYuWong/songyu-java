package com.songyu.commonutils;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

class CommonStringUtilsTest {

    @Test
    void encode() throws UnsupportedEncodingException {

        System.out.println(CommonStringUtils.encode("费用清点单", StandardCharsets.ISO_8859_1.name()));

    }
}