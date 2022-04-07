package com.pbteach.microservice.service2.service;

import com.pbteach.microservice.service2.api.ProviderService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class ProviderServiceImpl implements ProviderService {
    @Override
    public String service() {
        return "222-Provider invoke";
    }
}