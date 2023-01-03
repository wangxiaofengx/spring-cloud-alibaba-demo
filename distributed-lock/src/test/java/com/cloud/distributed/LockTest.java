package com.cloud.distributed;

import com.cloud.distributed.lock.annotation.DistributedLock;
import com.cloud.distributed.lock.test.BusinessTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DistributedLockBootstrap.class)
@Slf4j
public class LockTest {

    @Autowired
    CuratorFramework curatorFramework;

    ThreadPoolExecutor threadPoolExecutor;

    InterProcessLock interProcessLock;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    BusinessTest businessTest;

    @Before
    public void before() throws InterruptedException {
        int threads = Runtime.getRuntime().availableProcessors();
        threadPoolExecutor = new ThreadPoolExecutor(threads, threads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        interProcessLock = new InterProcessMutex(curatorFramework, "/locks");
        curatorFramework.start();
        curatorFramework.blockUntilConnected();
    }

    @Test
    public void test() throws InterruptedException {
        int loop = businessTest.getNumber();
        LocalDateTime begin = LocalDateTime.now();
        CountDownLatch countDownLatch = new CountDownLatch(loop);
        for (int i = 0; i < loop; i++) {
            threadPoolExecutor.execute(() -> {
                try {
                    distributedLock();
                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        countDownLatch.await();
        LocalDateTime end = LocalDateTime.now();
        Duration between = Duration.between(begin, end);
        log.info("number:{},elapsed time:{}", businessTest.getNumber(), between.toMillis());
    }

    public void zookeeperLock() throws Exception {
        interProcessLock.acquire();
        try {
            business();
        } finally {
            interProcessLock.release();
        }
    }

    public void redissonLock() {
        RLock locks = redissonClient.getLock("locks");
        locks.lock();
        try {
            business();
        } finally {
            locks.unlock();
        }

    }

    public void distributedLock(){
        business();
    }

    public int business() {
        return businessTest.business();
    }
}
