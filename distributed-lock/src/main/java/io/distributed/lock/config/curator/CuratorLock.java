package io.distributed.lock.config.curator;

import io.distributed.lock.DistributedLockI;
import io.distributed.lock.exception.DistributedLockException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * zookeeper锁实现
 *
 * @author wangxiaofeng
 * @date 2023/1/4 9:57
 */
public class CuratorLock implements DistributedLockI {

    public static final String MODE = "zookeeper";

    CuratorFramework curatorFramework;
    String path;

    InterProcessLock interProcessLock;

    public CuratorLock(CuratorFramework curatorFramework, String path) {
        this.curatorFramework = curatorFramework;
        this.path = path;
        this.interProcessLock = new InterProcessMutex(curatorFramework, path);
    }

    @Override
    public void lock() {
        try {
            this.interProcessLock.acquire();
        } catch (Exception e) {
            throw new DistributedLockException(e);
        }
    }

    @Override
    public void lock(long time, TimeUnit unit) {
        try {
            this.interProcessLock.acquire(time, unit);
        } catch (Exception e) {
            throw new DistributedLockException(e);
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException("zookeeper mode nonsupport lockInterruptibly");
    }

    @Override
    public boolean tryLock() {
        return this.interProcessLock.isAcquiredInThisProcess();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return this.interProcessLock.isAcquiredInThisProcess();
    }

    @Override
    public void unlock() {
        try {
            this.interProcessLock.release();
        } catch (Exception e) {
            throw new DistributedLockException(e);
        }
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("zookeeper mode nonsupport newCondition");
    }

    public void destroy(){
        this.curatorFramework.close();
    }
}
