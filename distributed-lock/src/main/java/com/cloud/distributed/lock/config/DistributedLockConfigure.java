package com.cloud.distributed.lock.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DistributedLockConfigure {

    @Bean
    @ConditionalOnBean(value = DistributedLockProperties.class)
    public DistributedLock distributedLockService(DistributedLockProperties distributedLockProperties) {
        DistributedLock distributedLock = new DistributedLock();
        distributedLock.setMode(distributedLockProperties.getMode());
        return distributedLock;
    }
}
