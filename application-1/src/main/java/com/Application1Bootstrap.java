package com;

import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.config.IClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class Application1Bootstrap {
    public static void main(String[] args) {
        SpringApplication.run(Application1Bootstrap.class, args);
    }

    @Bean
    public IClientConfig iClientConfig() {
        return new DefaultClientConfigImpl();
    }
}