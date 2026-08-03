package com.example.bookkeeping.purchase.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BizPurchase {
    private Long id;
    private String purchaseNo;
    private String platform;
    private String platformOrderNo;
    private String supplierName;
    private String sellerAccount;
    private LocalDateTime purchaseDate;
    private Integer itemCount;
    private Integer purchaseStatus;
    private BigDecimal goodsAmount;
    private BigDecimal freightAmount;
    private BigDecimal discountAmount;
    private BigDecimal otherAmount;
    private BigDecimal payAmount;
    private Integer invoiceStatus;
    private String invoiceTitle;
    private String invoiceNo;
    private LocalDate invoiceDate;
    private String invoiceFileName;
    private String invoiceFilePath;
    private String paymentMethod;
    private LocalDateTime receivedAt;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleted;
}
