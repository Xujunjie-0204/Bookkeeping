package com.example.bookkeeping.inventory.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BizInventoryBatch {
    private Long id;
    private String batchNo;
    private Long productId;
    private Long purchaseId;
    private Long purchaseItemId;
    private LocalDate purchaseDate;
    private BigDecimal unitCost;
    private Integer initialQuantity;
    private Integer availableQuantity;
    private Integer soldQuantity;
    private Integer lockedQuantity;
    private Integer status;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleted;
}
