package com.example.bookkeeping.dashboard.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductTopVO {
    private Integer rankNo;
    private String productName;
    private Integer quantity;
    private BigDecimal saleAmount;
    private BigDecimal profitAmount;
    private BigDecimal profitRate;
}
