package com.cloud.distributed.lock.config;

import com.cloud.distributed.lock.config.curator.CuratorLock;
import com.cloud.distributed.lock.config.redisson.RedissonLock;
import com.cloud.distributed.lock.exception.DistributedLockException;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class DistributedLock implements DistributedLockI {

    private String mode;

    private DistributedLockI zookeeperLock;

    private DistributedLockI redisLock;

    @Override
    public void lock() {
        this.getLock().lock();
    }

    @Override
    public void lock(long time, TimeUnit unit) {
        this.getLock().lock(time, unit);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        this.getLock().lockInterruptibly();
    }

    @Override
    public boolean tryLock() {
        return this.getLock().tryLock();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return this.getLock().tryLock(time, unit);
    }

    @Override
    public void unlock() {
        this.getLock().unlock();
    }

    @Override
    public Condition newCondition() {
        return this.getLock().newCondition();
    }

    public DistributedLockI getLock() {
        if (StringUtils.equalsIgnoreCase(mode, RedissonLock.MODE)) {
            if (this.redisLock == null) {
                throw new DistributedLockException("redis lock not exist");
            }
            return this.redisLock;
        }

        if (StringUtils.equalsIgnoreCase(mode, CuratorLock.MODE)) {
            if (this.zookeeperLock == null) {
                throw new DistributedLockException("zookeeper lock not exist");
            }
            return this.zookeeperLock;
        }

        throw new DistributedLockException("distributed lock not exist");
    }

    public String getMode() {
        return mode;
    }

    public DistributedLock setMode(String mode) {
        this.mode = mode;
        return this;
    }

    public DistributedLockI getZookeeperLock() {
        return zookeeperLock;
    }

    public DistributedLock setZookeeperLock(DistributedLockI zookeeperLock) {
        this.zookeeperLock = zookeeperLock;
        return this;
    }

    public DistributedLockI getRedisLock() {
        return redisLock;
    }

    public DistributedLock setRedisLock(DistributedLockI redisLock) {
        this.redisLock = redisLock;
        return this;
    }

}
