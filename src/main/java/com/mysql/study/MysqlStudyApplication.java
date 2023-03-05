package com.mysql.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MysqlStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MysqlStudyApplication.class, args);
    }

}
