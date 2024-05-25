package com.feirui.subject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 刷题服务入口启动类
 */
@SpringBootApplication
@ComponentScan("com.feirui")
@MapperScan("com.feirui.**.dao")
@EnableFeignClients(basePackages = "com.feirui.**.api")
public class SubjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(SubjectApplication.class, args);
    }
}
