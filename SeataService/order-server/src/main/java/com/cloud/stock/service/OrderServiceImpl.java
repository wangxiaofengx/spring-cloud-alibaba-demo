package com.cloud.stock.service;

import com.cloud.stock.api.OrderService;
import com.cloud.stock.api.StockService;
import com.cloud.stock.dto.OrderInfo;
import com.cloud.stock.dto.StockInfo;
import com.cloud.stock.mapper.OrderMapper;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@DubboService
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @DubboReference
    StockService stockService;

    @Override
    @GlobalTransactional
    public OrderInfo create(OrderInfo orderInfo) {

        log.info("OrderServiceImpl=====================>>create");

        Long id = orderMapper.create(orderInfo);
        orderInfo.setId(id);


        StockInfo stockInfo = new StockInfo();
        stockInfo.setStatus(1);
        stockInfo.setName("ttttt");
        stockService.create(stockInfo);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return orderInfo;
    }

    @Override
    public List<OrderInfo> findAll() {
        return orderMapper.findAll();
    }

}
