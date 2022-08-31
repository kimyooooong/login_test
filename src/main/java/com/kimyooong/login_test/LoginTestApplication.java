package com.kimyooong.login_test;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Log4j2
public class LoginTestApplication {


    @Value("${server.config}")
    String config;

    public static void main(String[] args) {
        SpringApplication.run(LoginTestApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> log.info("server config : {}", config);
    }

}
