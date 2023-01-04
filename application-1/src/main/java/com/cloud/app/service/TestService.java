package com.cloud.app.service;

import io.distributed.lock.annotation.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestService {

    int number;

    @DistributedLock
    public int inc() {
        this.number--;
        return this.number;
    }

    public int getNumber() {
        return number;
    }

    public TestService setNumber(int number) {
        this.number = number;
        return this;
    }
}
