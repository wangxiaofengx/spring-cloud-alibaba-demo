package com.pbteach.microservice.application1.controller;

import com.pbteach.microservice.service1.api.ConsumerService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Application1Controller {

    @Reference
    private ConsumerService consumerService;

    @GetMapping("/service")
    public String service() {
        return "test";
    }

    @GetMapping("/rpcService")
    public String rpcService() {
        return "test" + consumerService.service();
    }
}