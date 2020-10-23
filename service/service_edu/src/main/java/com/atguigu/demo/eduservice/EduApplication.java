package com.atguigu.demo.eduservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// 启动类
@SpringBootApplication
@EnableDiscoveryClient // 添加Nacos客户端注解
@EnableFeignClients // 用来服务调用
@ComponentScan(basePackages = {"com.atguigu"}) // 设置包的扫描规则,默认只扫本项目的
@EnableSwagger2
public class EduApplication {
    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class, args);
    }
}
