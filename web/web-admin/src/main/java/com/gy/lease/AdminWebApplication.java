package com.gy.lease;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.gy.lease.web.*.mapper")
@EnableScheduling
public class AdminWebApplication  {
    public static void main(String[] args) {SpringApplication.run(AdminWebApplication.class, args);}


}