package com.example.bookkeeping.sale.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SaleStockVO {
    private Long batchId;
    private String batchNo;
    private Long productId;
    private String productCode;
    private String productName;
    private String productTypeName;
    private String brand;
    private String model;
    private String specification;
    private Long purchaseId;
    private Long purchaseItemId;
    private String purchaseNo;
    private LocalDate purchaseDate;
    private String platform;
    private String platformOrderNo;
    private String sellerAccount;
    private String deviceNo;
    private String conditionDesc;
    private Integer availableQuantity;
    private BigDecimal unitCost;
    private BigDecimal defaultSalePrice;
}
