package com.cloud.service1.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.cloud.service2.api.ProviderService;
import com.cloud.service1.api.ConsumerService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class ConsumerServiceImpl implements ConsumerService {

    @DubboReference
    ProviderService providerService;

    @Override
    @SentinelResource(value = "service1")
    public String service() {
        return "Consumer invoke | " + providerService.service();
    }
}