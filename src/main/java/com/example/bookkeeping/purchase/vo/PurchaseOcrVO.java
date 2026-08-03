package com.example.bookkeeping.purchase.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseOcrVO {
    private String platform;
    private String platformOrderNo;
    private String supplierName;
    private String sellerAccount;
    private String purchaseDate;
    private String paymentMethod;
    private BigDecimal payAmount;
    private String expressName;
    private String productTitle;
    private String conditionDesc;
    private String rawText;
}
