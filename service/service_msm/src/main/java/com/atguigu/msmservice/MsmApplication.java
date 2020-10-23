package com.atguigu.msmservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)// 取消数据源自动配置
@ComponentScan("com.atguigu")
public class MsmApplication {

        public static void main(String[] args) {
        SpringApplication.run(MsmApplication.class, args);
    }
}

