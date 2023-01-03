package com.cloud.distributed.lock.test;

import com.cloud.distributed.lock.annotation.DistributedLock;
import org.springframework.stereotype.Component;

@Component
public class BusinessTest {

    int number = 5000;

    @DistributedLock
    public int business() {
        number--;
        return number;
    }

    public int getNumber() {
        return number;
    }
}
