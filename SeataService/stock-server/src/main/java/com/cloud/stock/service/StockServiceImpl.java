package com.cloud.stock.service;

import com.cloud.stock.api.StockService;
import com.cloud.stock.dto.StockInfo;
import com.cloud.stock.mapper.StockMapper;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;
import java.util.concurrent.TimeUnit;

@DubboService
public class StockServiceImpl implements StockService {

    final StockMapper stockMapper;

    public StockServiceImpl(StockMapper orderMapper) {
        this.stockMapper = orderMapper;
    }

    @Override
    @GlobalTransactional
    public StockInfo create(StockInfo stockInfo) {
        Long id = stockMapper.create(stockInfo);
        stockInfo.setId(id);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return stockInfo;
    }

    @Override
    public List<StockInfo> findAll() {
        return stockMapper.findAll();
    }

}
