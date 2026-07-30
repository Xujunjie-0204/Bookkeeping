package com.example.bookkeeping.sale.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleItemVO {
    private Long id;
    private Long productId;
    private String productCode;
    private String productName;
    private String productTypeName;
    private String brand;
    private String model;
    private String deviceNo;
    private String conditionDesc;
    private Long batchId;
    private String batchNo;
    private Long purchaseId;
    private Long purchaseItemId;
    private String purchaseNo;
    private Integer quantity;
    private BigDecimal saleUnitPrice;
    private BigDecimal saleAmount;
    private BigDecimal costUnitPrice;
    private BigDecimal costAmount;
    private BigDecimal profitAmount;
    private String remark;
}
