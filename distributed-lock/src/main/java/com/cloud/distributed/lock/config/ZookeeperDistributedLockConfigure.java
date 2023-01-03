package com.cloud.distributed.lock.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperDistributedLockConfigure {

    @Autowired
    CuratorConfig curatorConfig;

    @Bean
    public CuratorFramework curatorFramework() {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(curatorConfig.elapsedTimeMs, curatorConfig.retryCount);

        return CuratorFrameworkFactory.builder().connectString(curatorConfig.connectString).
                sessionTimeoutMs(curatorConfig.sessionTimeoutMs).
                connectionTimeoutMs(curatorConfig.connectionTimeoutMs).
                retryPolicy(retryPolicy).
                build();
    }
}
