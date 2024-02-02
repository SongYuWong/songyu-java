package com.songyu.domains.auth.entity;


import com.songyu.domains.auth.valueObject.UserStatus;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void crypto(){
        User user = new User();
        user.setUserCode("DN007");
        user.setUserName("DNMT");
        user.setUserDesc("大内密探");
        user.setUserEmail("songyuwong@163.com");
        user.setUserTel("110");
        user.setUserPassword("KMKCJLYasdfasd");
        user.setUserStatusCode(UserStatus.VIP.ordinal());
        user.encryptInfo();
        System.out.println(user.toInsertSql());
    }

}