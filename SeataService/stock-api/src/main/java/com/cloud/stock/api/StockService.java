package com.cloud.stock.api;

import com.cloud.stock.dto.StockInfo;

import java.util.List;

public interface StockService {

    StockInfo create(StockInfo stockInfo);

    List<StockInfo> findAll();
}
