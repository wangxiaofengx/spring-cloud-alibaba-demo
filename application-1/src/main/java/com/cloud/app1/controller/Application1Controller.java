package com.cloud.app1.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.cloud.service1.api.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class Application1Controller {

    @DubboReference
    private ConsumerService consumerService;

    @GetMapping("/service")
    @SentinelResource(value = "/service")
    public String service() {
        log.info(Thread.currentThread().getId() + "");
        return "test" + consumerService.service();
    }

    // Fallback 函数，函数签名与原函数一致或加一个 Throwable 类型的参数.
    public String serviceFallback() {
        log.info(Thread.currentThread().getId() + "");
        return "Halooooo";
    }

    // Block 异常处理函数，参数最后多一个 BlockException，其余与原函数一致.
    public String exceptionHandler(BlockException ex) {
        System.out.println("exceptionHandler");
        // Do some log here.
        ex.printStackTrace();
        return "Oops, error occurred at ";
    }

    @RequestMapping(value = "/echo/{string}", method = RequestMethod.GET)
    public String echo(@PathVariable String string) {
        return "hello Nacos Discovery " + string;
    }

    @RequestMapping(value = "/divide", method = RequestMethod.GET)
    public String divide(@RequestParam Integer a, @RequestParam Integer b) {
        return String.valueOf(a / b);
    }
}