package com.songyu.components.springboot.stater;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnvSecureUtilTest {

    private static final String password = "asd";

    @Test
    void encrypt() {
        System.out.println(EnvSecureUtil.encrypt("asddd", password));
    }

    @Test
    void decrypt() {
        System.out.println(EnvSecureUtil.decrypt("+ACti8GaRj5uiC+bOZYCo0nED4nHbCEV0V8v7uYQGmE=", password));
    }
}