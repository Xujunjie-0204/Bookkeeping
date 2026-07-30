package com.example.bookkeeping.dashboard.vo;

import lombok.Data;

import java.util.List;

@Data
public class DashboardChartVO {
    private List<SalesTrendVO> salesTrend;
    private List<SalesTrendVO> profitTrend;
    private List<ExpensePieVO> expensePie;
}
