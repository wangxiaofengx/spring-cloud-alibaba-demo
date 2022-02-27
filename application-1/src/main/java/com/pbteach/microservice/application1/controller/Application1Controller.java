package com.pbteach.microservice.application1.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.pbteach.microservice.service1.api.ConsumerService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Application1Controller {

    @DubboReference
    private ConsumerService consumerService;

    @GetMapping("/service")
    @SentinelResource(value = "service", blockHandler = "exceptionHandler")
    public String service() {
        return "test" + consumerService.service();
    }

    // Fallback 函数，函数签名与原函数一致或加一个 Throwable 类型的参数.
    public String serviceFallback() {
        System.out.println("serviceFallback");
        return "Halooooo";
    }

    // Block 异常处理函数，参数最后多一个 BlockException，其余与原函数一致.
    public String exceptionHandler(BlockException ex) {
        System.out.println("exceptionHandler");
        // Do some log here.
        ex.printStackTrace();
        return "Oops, error occurred at ";
    }
}