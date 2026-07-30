package com.example.bookkeeping.purchase.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseQueryRequest {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String keyword;
    private Integer purchaseStatus;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    private List<Long> productIds;
}
