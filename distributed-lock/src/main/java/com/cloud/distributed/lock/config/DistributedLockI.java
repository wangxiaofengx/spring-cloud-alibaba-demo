package com.cloud.distributed.lock.config;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public interface DistributedLockI extends Lock {

    void lock(long time, TimeUnit unit);
}
