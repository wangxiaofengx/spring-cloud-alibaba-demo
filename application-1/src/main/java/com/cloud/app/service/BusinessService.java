package com.cloud.app.service;

import io.distributed.lock.annotation.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
public class BusinessService {

    @Autowired
    ExecutorService executorService;

    @Autowired
    BusinessService businessService;

    @Autowired
    TestService testService;

    int number;

    public String concurrentTest(int number) throws InterruptedException {
        businessService.setNumber(number);
        int loop = number;
        LocalDateTime begin = LocalDateTime.now();
        CountDownLatch countDownLatch = new CountDownLatch(loop);
        for (int i = 0; i < loop; i++) {
            executorService.execute(() -> {
                try {
                    businessService.inc();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        LocalDateTime end = LocalDateTime.now();
        Duration between = Duration.between(begin, end);
        String result = String.format("number:%d,elapsed time:%d", businessService.getNumber(), between.toMillis());
        return result;
    }


    @DistributedLock
    public int inc() {
        this.number--;
//        businessService.testReentrantLock();
        return this.number;
    }

    @DistributedLock
    public void testReentrantLock() {

    }

    public int getNumber() {
        return number;
    }

    public BusinessService setNumber(int number) {
        this.number = number;
        return this;
    }
}
