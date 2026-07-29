package com.example.bookkeeping.purchase.dto;

import lombok.Data;

@Data
public class PurchaseQueryRequest {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String keyword;
    private Integer purchaseStatus;
}
