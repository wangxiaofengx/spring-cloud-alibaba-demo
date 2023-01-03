package com.cloud.distributed.lock.config.redisson;

import com.cloud.distributed.lock.config.DistributedLock;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfigure {

    @Bean
    @ConditionalOnBean(value = RedissonProperties.class)
    @ConditionalOnProperty(prefix = "distributed.lock", name = "mode", havingValue = RedissonLock.MODE)
    public RedissonLock redissonClient(RedissonProperties redissonProperties, DistributedLock distributedLock) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + redissonProperties.single.address);
        RedissonClient redissonClient = Redisson.create();
        RedissonLock redissonLock = new RedissonLock(redissonClient, redissonProperties.path);
        distributedLock.setRedisLock(redissonLock);
        return redissonLock;
    }
}
