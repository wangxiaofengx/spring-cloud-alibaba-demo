package com.cloud.distributed.lock.config.redisson;

import com.cloud.distributed.lock.config.DistributedLockI;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "distributed.lock.redis")
public class RedissonProperties {

    Single single;

    String path = DistributedLockI.PATH;

    @Data
    public static class Single {
        String address;
    }
}
