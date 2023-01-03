package com.cloud.distributed.lock.config.curator;

import com.cloud.distributed.lock.config.DistributedLockI;
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

    String path = DistributedLockI.PATH;
}
