package com.example.bookkeeping.sale.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SaleVO {
    private Long id;
    private String recordNo;
    private LocalDate businessDate;
    private String platform;
    private String platformOrderNo;
    private String productName;
    private String purchaseNo;
    private String buyerName;
    private String buyerPhone;
    private Integer itemCount;
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
}
