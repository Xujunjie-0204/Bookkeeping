package com.example.bookkeeping.sale.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleSummaryVO {
    private Integer orderCount;
    private BigDecimal totalSaleAmount;
    private BigDecimal totalProfitAmount;
}
