package com.cloud.app1.controller;

import com.cloud.stock.api.OrderService;
import com.cloud.stock.dto.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @DubboReference
    OrderService orderService;

    @GetMapping("/list")
    public List<OrderInfo> list() {
        List<OrderInfo> all = orderService.findAll();
       return all;
    }

    @GetMapping("/create")
    public OrderInfo create() {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setName("test");
        orderInfo.setStatus(1);
        OrderInfo result = orderService.create(orderInfo);
        return result;
    }
}
