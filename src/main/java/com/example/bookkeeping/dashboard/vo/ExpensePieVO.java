package com.example.bookkeeping.dashboard.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExpensePieVO {
    private String name;
    private BigDecimal value;
}
