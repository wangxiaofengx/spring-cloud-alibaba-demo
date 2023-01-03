package com.cloud.distributed.lock.annotation;

import java.lang.annotation.*;

/**
 * 分布式锁注解
 *
 * @author wangxiaofeng
 * @date 2023/1/3 15:37
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
@Inherited
public @interface DistributedLock {
}
