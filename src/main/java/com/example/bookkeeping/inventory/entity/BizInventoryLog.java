package com.example.bookkeeping.inventory.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BizInventoryLog {
    private Long id;
    private Long productId;
    private Long batchId;
    private String businessType;
    private Long businessId;
    private String businessNo;
    private Integer changeQuantity;
    private Integer beforeQuantity;
    private Integer afterQuantity;
    private BigDecimal unitCost;
    private LocalDate businessDate;
    private String remark;
    private LocalDateTime createdAt;
}
