package com.cloud.app.controller;

import com.cloud.app.service.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/distributed/lock")
@Slf4j
public class DistributedLockController {


    @Autowired
    RedissonClient redissonClient;

    @Autowired
    BusinessService businessService;

    @GetMapping("/reset")
    public int before() {
        RBucket<Integer> bucket = redissonClient.getBucket("number");
        bucket.set(1000);

        RBucket<Integer> bucket2 = redissonClient.getBucket("number2");
        bucket2.set(1000);

        RBucket<Integer> bucket3 = redissonClient.getBucket("number3");
        bucket3.set(1000);

        return bucket.get() + bucket2.get() + bucket3.get();
    }

    @GetMapping("/update")
    public int update() {
        return businessService.update();
    }

    @GetMapping("/get")
    public int get() {
        RBucket<Integer> bucket = redissonClient.getBucket("number");
        RBucket<Integer> bucket2 = redissonClient.getBucket("number2");
        RBucket<Integer> bucket3 = redissonClient.getBucket("number3");
        return bucket.get() + bucket2.get() + bucket3.get();
    }


    @GetMapping("/decReset")
    public int decBefore() {
        RAtomicLong atomicLong = this.redissonClient.getAtomicLong("decNumber");
        atomicLong.set(1000);
        return (int) atomicLong.get();
    }

    @GetMapping("/dec")
    public int dec() {
        return businessService.dec();
    }

    @GetMapping("/decGet")
    public int decGet() {
        RAtomicLong atomicLong = this.redissonClient.getAtomicLong("decNumber");
        return (int) atomicLong.get();
    }

}
