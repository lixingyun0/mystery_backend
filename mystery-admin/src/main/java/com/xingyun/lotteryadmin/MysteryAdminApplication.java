package com.xingyun.lotteryadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.xingyun.lotteryadmin", "com.xingyun.mysterycommon"})
@MapperScan("com.xingyun.lotterycommon.dao.mapper")
@EnableTransactionManagement
public class MysteryAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MysteryAdminApplication.class, args);
    }

}
