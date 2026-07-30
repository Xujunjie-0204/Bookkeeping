package com.example.bookkeeping.dashboard.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesTrendVO {
    private String date;
    private BigDecimal amount;
}
