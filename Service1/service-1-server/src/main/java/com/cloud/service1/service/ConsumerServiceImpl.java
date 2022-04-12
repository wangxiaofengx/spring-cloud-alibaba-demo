package com.cloud.service1.service;

import com.cloud.service1.api.ConsumerService;
import com.cloud.service2.api.ProviderService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class ConsumerServiceImpl implements ConsumerService {

    @DubboReference
    ProviderService providerService;

    @Override
    public String service() {
        return "Consumer invoke | " + providerService.service();
    }
}