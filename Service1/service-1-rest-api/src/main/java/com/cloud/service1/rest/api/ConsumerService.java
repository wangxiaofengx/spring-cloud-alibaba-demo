package com.cloud.service1.rest.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("service1")
public interface ConsumerService {

    @GetMapping("/consumer/service")
    String service();
}