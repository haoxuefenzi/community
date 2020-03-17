package com.wang.my_community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Repository;

@SpringBootApplication
@MapperScan("com.wang.my_community.mapper")
public class MyCommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyCommunityApplication.class, args);
    }

}
