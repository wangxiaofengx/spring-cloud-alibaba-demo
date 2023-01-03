package com.cloud.distributed.lock.annotation;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.concurrent.locks.Lock;

public class DistributedLockInterceptor implements MethodInterceptor {

    Lock lock;

    public DistributedLockInterceptor(Lock lock) {
        this.lock = lock;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        this.lock.lock();
        Object proceed = invocation.proceed();
        this.lock.unlock();
        return proceed;
    }
}
