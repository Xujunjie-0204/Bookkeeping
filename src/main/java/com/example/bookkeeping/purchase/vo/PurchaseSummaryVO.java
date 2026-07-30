package com.example.bookkeeping.purchase.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseSummaryVO {
    private Long orderCount;
    private BigDecimal totalPurchaseAmount;
}
