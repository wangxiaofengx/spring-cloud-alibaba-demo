package com;

import feign.Logger;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages={"com.cloud"})
@EnableDiscoveryClient
@EnableDubbo
@EnableFeignClients
public class Application1Bootstrap {
    public static void main(String[] args) {
        SpringApplication.run(Application1Bootstrap.class, args);
    }

    @Bean
    Logger.Level feignLogLevel(){
        return Logger.Level.FULL;
    }
}