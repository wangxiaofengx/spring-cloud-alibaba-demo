package com.cloud.distributed.lock.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "curator")
public class CuratorConfig {

    String connectString;

    int sessionTimeoutMs;

    int connectionTimeoutMs;

    int elapsedTimeMs;

    int retryCount;
}
