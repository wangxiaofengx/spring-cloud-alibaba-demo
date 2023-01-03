package com.cloud.distributed.lock.config;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public interface DistributedLockI extends Lock {

    String PATH = "/" + UUID.randomUUID().toString();

    void lock(long time, TimeUnit unit);
}
