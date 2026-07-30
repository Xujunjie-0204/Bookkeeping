package com.example.bookkeeping.sale.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class SaveSaleRequest {
    private String recordNo;

    @NotNull(message = "销售日期不能为空")
    private LocalDate businessDate;

    @NotBlank(message = "销售平台不能为空")
    private String platform;

    private String platformOrderNo;
    private String buyerName;
    private String buyerPhone;
    private BigDecimal platformFee;
    private BigDecimal expressFee;
    private BigDecimal packageFee;
    private BigDecimal promotionFee;
    private BigDecimal refundAmount;
    private BigDecimal otherExpense;
    private Integer paymentStatus;
    private Integer shipmentStatus;
    private String expressCompany;
    private String expressNo;
    private String remark;

    @Valid
    @NotEmpty(message = "请至少选择一个销售商品")
    private List<SaveSaleItemRequest> items;
}
