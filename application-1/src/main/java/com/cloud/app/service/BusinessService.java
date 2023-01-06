package com.cloud.app.service;

import io.distributed.lock.annotation.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
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

    @Autowired
    RedissonClient redissonClient;

    int number;

    public String concurrentTest(int number) throws InterruptedException {
        businessService.setNumber(number);
        int loop = number;
        LocalDateTime begin = LocalDateTime.now();
        CountDownLatch countDownLatch = new CountDownLatch(loop);
        for (int i = 0; i < loop; i++) {
            executorService.execute(() -> {
                try {
                    businessService.dec();
                } catch (Exception e) {
                    throw new RuntimeException(e);
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

    public int dec() {
        RAtomicLong atomicLong = this.redissonClient.getAtomicLong("decNumber");
        return (int) atomicLong.decrementAndGet();
    }

    @DistributedLock(name = "update1")
    public int update() {
        RBucket<Integer> bucket = this.redissonClient.getBucket("number");
        Integer integer = bucket.get();
        integer--;
        bucket.set(integer);
//        Future<?> submit = executorService.submit(() -> businessService.update2());
//        try {
//            submit.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        businessService.update2();
        return integer;
    }

    @DistributedLock(name = "update2")
    public int update2() {
        RBucket<Integer> bucket = this.redissonClient.getBucket("number");
        Integer integer = bucket.get();
        integer--;
        bucket.set(integer);
//        Future<?> submit = executorService.submit(() -> businessService.update3());
//        try {
//            submit.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        businessService.update3();
        return integer;
    }

    @DistributedLock(name = "update1")
    public int update3() {
        RBucket<Integer> bucket = this.redissonClient.getBucket("number");
        Integer integer = bucket.get();
        integer--;
        bucket.set(integer);
        return integer;
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
