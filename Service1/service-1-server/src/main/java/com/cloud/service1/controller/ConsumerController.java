package com.cloud.service1.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consumer")
@Slf4j
public class ConsumerController {

    @GetMapping("/service")
    public String service() {

        return "service-1-rest-service";
    }
}
