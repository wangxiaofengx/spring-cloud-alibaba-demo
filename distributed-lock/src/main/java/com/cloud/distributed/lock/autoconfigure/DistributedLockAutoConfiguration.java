package com.cloud.distributed.lock.autoconfigure;

import com.cloud.distributed.lock.annotation.DistributedLockScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DistributedLockAutoConfiguration {

    @Bean
    public DistributedLockScanner distributedLockScanner() {
        DistributedLockScanner distributedLockScanner = new DistributedLockScanner();
        return distributedLockScanner;
    }
}
