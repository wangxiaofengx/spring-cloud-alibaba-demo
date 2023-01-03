package com.cloud.distributed;

import com.cloud.distributed.lock.test.BusinessTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    BusinessTest businessTest;

    @Before
    public void before() throws InterruptedException {
        int threads = Runtime.getRuntime().availableProcessors();
        threadPoolExecutor = new ThreadPoolExecutor(threads, threads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    @Test
    public void test() throws InterruptedException {
        int loop = businessTest.getNumber();
        LocalDateTime begin = LocalDateTime.now();
        CountDownLatch countDownLatch = new CountDownLatch(loop);
        for (int i = 0; i < loop; i++) {
            threadPoolExecutor.execute(() -> {
                try {
                    businessTest.business();
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
}
