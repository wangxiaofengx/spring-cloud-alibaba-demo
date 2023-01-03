package com.cloud.distributed.lock.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "distributed.lock")
public class DistributedLockProperties {

    String mode;
}
