package com.cloud.stock.api;

import com.cloud.stock.dto.OrderInfo;

import java.util.List;

public interface OrderService {

    OrderInfo create(OrderInfo orderInfo);

    List<OrderInfo> findAll();
}
