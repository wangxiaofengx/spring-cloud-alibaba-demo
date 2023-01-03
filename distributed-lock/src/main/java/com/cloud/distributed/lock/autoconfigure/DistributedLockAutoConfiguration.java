package com.cloud.distributed.lock.autoconfigure;

import com.cloud.distributed.lock.annotation.DistributedLockScanner;
import com.cloud.distributed.lock.config.DistributedLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = "com.cloud.distributed.lock.config")
@Configuration
public class DistributedLockAutoConfiguration {

    @Bean
    public DistributedLockScanner distributedLockScanner(DistributedLock distributedLock) {
        DistributedLockScanner distributedLockScanner = new DistributedLockScanner(distributedLock);
        return distributedLockScanner;
    }
}
