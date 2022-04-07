package com;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages={"com.cloud"})
@EnableDiscoveryClient
@EnableDubbo
public class Service2Bootstrap {

    public static void main(String[] args) {
        SpringApplication.run(Service2Bootstrap.class, args);
    }
}
