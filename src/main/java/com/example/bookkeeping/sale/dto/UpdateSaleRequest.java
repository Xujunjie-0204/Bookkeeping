package com.example.bookkeeping.sale.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class UpdateSaleRequest {
    @NotNull(message = "销售日期不能为空")
    private LocalDate businessDate;

    @NotBlank(message = "销售平台不能为空")
    private String platform;

    private String platformOrderNo;
    private String buyerName;
    private String buyerPhone;
    private Integer paymentStatus;
    private Integer shipmentStatus;
    private String expressCompany;
    private String expressNo;
    private String remark;
}
