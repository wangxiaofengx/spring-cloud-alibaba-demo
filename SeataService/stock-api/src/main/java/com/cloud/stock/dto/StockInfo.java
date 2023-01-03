package com.cloud.stock.dto;

import lombok.Data;

@Data
public class StockInfo {

    private Long id;

    private String name;

    private int amount;

    private Integer status;
}
