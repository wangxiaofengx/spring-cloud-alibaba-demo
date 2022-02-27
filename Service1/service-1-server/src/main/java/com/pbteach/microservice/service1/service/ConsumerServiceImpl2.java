package com.pbteach.microservice.service1.service;

import com.pbteach.microservice.service1.api.ConsumerService;
import com.pbteach.microservice.service2.api.ProviderService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class ConsumerServiceImpl2 implements ConsumerService {

    @DubboReference
    ProviderService providerService;

    @Override
    public String service() {
        return "Consumer2 invoke | " + providerService.service();
    }
}