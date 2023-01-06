package com.cloud.app.config;

import io.distributed.lock.config.redisson.RedissonProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfigure {

    @Bean
    @ConditionalOnBean(value =  {RedissonProperties.class})
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient(RedissonProperties redissonProperties) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + redissonProperties.getSingle().getAddress());
        RedissonClient redissonClient = Redisson.create();
        return redissonClient;
    }
}
