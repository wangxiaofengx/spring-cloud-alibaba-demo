package com.cloud.distributed.lock.config;

import com.cloud.distributed.lock.annotation.DistributedLockService;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DistributedLockConfigure {

    @Bean
    @ConditionalOnBean(value = DistributedLockProperties.class)
    public DistributedLockService distributedLockService(DistributedLockProperties distributedLockProperties, RedissonClient redissonClient, CuratorFramework curatorFramework) {

        DistributedLockService distributedLockService = new DistributedLockService();
        return distributedLockService;
    }
}
