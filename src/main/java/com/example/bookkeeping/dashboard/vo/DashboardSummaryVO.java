package com.example.bookkeeping.dashboard.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardSummaryVO {
    private BigDecimal todaySale;
    private BigDecimal todayProfit;
    private BigDecimal monthSale;
    private BigDecimal monthProfit;
    private BigDecimal quarterSale;
    private BigDecimal quarterProfit;
    private Integer inventoryQuantity;
    private BigDecimal inventoryCost;
    private BigDecimal quarterTarget;
    private BigDecimal quarterRemain;
    private BigDecimal quarterPercent;
}
