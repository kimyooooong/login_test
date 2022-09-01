package com.kimyooong.login_test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LoginTestApplicationTests {

    @Test
    void contextLoads() {

        int randomNumber = (int)(Math.random() * 1000000);
        System.out.println(randomNumber);
    }

}
