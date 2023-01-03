package com.cloud.distributed.lock.config.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CuratorConfigure {

    @Bean
    @ConditionalOnBean(value = CuratorProperties.class)
    public CuratorFramework curatorFramework(CuratorProperties curatorProperties) {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(curatorProperties.elapsedTimeMs, curatorProperties.retryCount);

        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(curatorProperties.connectString).
                sessionTimeoutMs(curatorProperties.sessionTimeoutMs).
                connectionTimeoutMs(curatorProperties.connectionTimeoutMs).
                retryPolicy(retryPolicy).
                build();
        return client;
    }
}
