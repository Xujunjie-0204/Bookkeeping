package com.example.bookkeeping.sale.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleOcrVO {
    private String platform;
    private String platformOrderNo;
    private String buyerName;
    private String buyerPhone;
    private String businessDate;
    private String expressCompany;
    private String expressNo;
    private Integer paymentStatus;
    private Integer shipmentStatus;
    private BigDecimal saleAmount;
    private String productTitle;
    private String conditionDesc;
    private String rawText;
}
