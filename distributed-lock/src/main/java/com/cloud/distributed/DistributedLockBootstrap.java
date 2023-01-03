package com.cloud.distributed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.cloud"})
public class DistributedLockBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(DistributedLockBootstrap.class, args);
    }
}