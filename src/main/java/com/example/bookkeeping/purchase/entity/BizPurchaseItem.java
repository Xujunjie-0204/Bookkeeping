package com.example.bookkeeping.purchase.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BizPurchaseItem {
    private Long id;
    private Long purchaseId;
    private Long productId;
    private String conditionDesc;
    private String deviceNo;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private Integer checkStatus;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleted;
}
