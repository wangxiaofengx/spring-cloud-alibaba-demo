package com.pbteach.microservice.service1.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.pbteach.microservice.service1.api.ConsumerService;
import com.pbteach.microservice.service2.api.ProviderService;
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