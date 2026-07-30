package com.example.bookkeeping.purchase.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class UpdatePurchaseRequest {
    @NotBlank(message = "采购平台不能为空")
    @Size(max = 30, message = "采购平台长度不能超过30位")
    private String platform;

    @Size(max = 80, message = "平台订单号长度不能超过80位")
    private String platformOrderNo;

    @Size(max = 100, message = "供应商长度不能超过100位")
    private String supplierName;

    @Size(max = 100, message = "卖家账号长度不能超过100位")
    private String sellerAccount;

    @NotNull(message = "采购日期不能为空")
    private LocalDate purchaseDate;

    @Size(max = 50, message = "付款方式长度不能超过50位")
    private String paymentMethod;

    @Size(max = 500, message = "备注长度不能超过500位")
    private String remark;

    @Valid
    private List<UpdatePurchaseItemRequest> items = new ArrayList<UpdatePurchaseItemRequest>();
}
