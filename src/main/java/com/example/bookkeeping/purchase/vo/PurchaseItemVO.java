package com.example.bookkeeping.purchase.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseItemVO {
    private Long id;
    private Long productId;
    private String productCode;
    private String productName;
    private String productTypeName;
    private String brand;
    private String model;
    private String specification;
    private String conditionDesc;
    private String deviceNo;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private Integer checkStatus;
    private String remark;
}
