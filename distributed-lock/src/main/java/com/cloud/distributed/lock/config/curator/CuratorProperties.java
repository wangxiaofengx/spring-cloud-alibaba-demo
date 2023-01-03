package com.cloud.distributed.lock.config.curator;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "distributed.lock.zookeeper")
public class CuratorProperties {

    String connectString;

    int sessionTimeoutMs;

    int connectionTimeoutMs;

    int elapsedTimeMs;

    int retryCount;

    String path;
}
