package com.pbteach.microservice.service1.service;

import com.pbteach.microservice.service1.api.ConsumerService;
import com.pbteach.microservice.service2.api.ProviderService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Reference
    ProviderService providerService;

    @Override
    public String service() {
        return "Consumer invoke | " + providerService.service();
    }
}