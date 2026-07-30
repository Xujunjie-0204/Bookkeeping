package com.example.bookkeeping.sale.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BizSaleRecord {
    private Long id;
    private String recordNo;
    private LocalDate businessDate;
    private String platform;
    private String platformOrderNo;
    private String buyerName;
    private String buyerPhone;
    private BigDecimal totalSaleAmount;
    private BigDecimal totalCostAmount;
    private BigDecimal platformFee;
    private BigDecimal expressFee;
    private BigDecimal packageFee;
    private BigDecimal promotionFee;
    private BigDecimal refundAmount;
    private BigDecimal otherExpense;
    private String feeConfig;
    private BigDecimal receivedAmount;
    private BigDecimal profitAmount;
    private Integer paymentStatus;
    private Integer shipmentStatus;
    private String expressCompany;
    private String expressNo;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleted;
}
