package com.pbteach.microservice.service1.service;

import com.pbteach.microservice.service1.api.ConsumerService;
import com.pbteach.microservice.service2.api.ProviderService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;

//@DubboService
public class ConsumerServiceImpl implements ConsumerService {

    @DubboReference
    ProviderService providerService;

    @Override
    public String service() {
        return "Consumer invoke | " + providerService.service();
    }
}