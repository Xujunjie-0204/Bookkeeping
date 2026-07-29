package com.example.bookkeeping.purchase.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PurchaseVO {
    private Long id;
    private String purchaseNo;
    private String platform;
    private String platformOrderNo;
    private String supplierName;
    private String sellerAccount;
    private LocalDate purchaseDate;
    private Integer itemCount;
    private Integer purchaseStatus;
    private BigDecimal goodsAmount;
    private BigDecimal freightAmount;
    private BigDecimal discountAmount;
    private BigDecimal otherAmount;
    private BigDecimal payAmount;
    private String paymentMethod;
    private LocalDateTime receivedAt;
    private String remark;
    private LocalDateTime createdAt;
}
