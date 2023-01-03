package com.cloud.distributed.lock.config.redisson;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "distributed.lock.redis")
public class RedissonProperties {

    Single single;

    String path;

    @Data
    public static class Single {
        String address;
    }
}
