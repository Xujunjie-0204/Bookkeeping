package com.example.bookkeeping.inventory.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InventorySummaryVO {
    private Long productId;
    private String productCode;
    private String productName;
    private Long productTypeId;
    private String productTypeName;
    private String brand;
    private String model;
    private String specification;
    private Integer warningStock;
    private Integer initialQuantity;
    private Integer availableQuantity;
    private Integer lockedQuantity;
    private Integer soldQuantity;
    private BigDecimal stockAmount;
}
