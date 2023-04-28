package com.xingyun.mysteryapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.xingyun.mysteryapi", "com.xingyun.mysterycommon"})
@MapperScan("com.xingyun.mysterycommon.dao.mapper")
@EnableTransactionManagement
@EnableScheduling
public class MysteryApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MysteryApiApplication.class, args);
    }

}
