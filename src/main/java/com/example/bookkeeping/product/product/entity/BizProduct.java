package com.example.bookkeeping.product.product.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BizProduct {

    private Long id;
    private String productCode;
    private String productName;
    private Long productTypeId;
    private String categoryName;
    private String brand;
    private String model;
    private String specification;
    private BigDecimal defaultCost;
    private BigDecimal defaultSalePrice;
    private Integer warningStock;
    private Integer status;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleted;
}
