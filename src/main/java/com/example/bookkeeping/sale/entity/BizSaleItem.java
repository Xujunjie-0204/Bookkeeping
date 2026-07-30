package com.example.bookkeeping.sale.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BizSaleItem {
    private Long id;
    private Long saleRecordId;
    private Long productId;
    private Long batchId;
    private Long purchaseId;
    private Long purchaseItemId;
    private Integer quantity;
    private BigDecimal saleUnitPrice;
    private BigDecimal saleAmount;
    private BigDecimal costUnitPrice;
    private BigDecimal costAmount;
    private BigDecimal profitAmount;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleted;
}
