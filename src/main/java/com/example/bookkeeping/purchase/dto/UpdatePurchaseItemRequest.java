package com.example.bookkeeping.purchase.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdatePurchaseItemRequest {
    @NotNull(message = "采购明细不能为空")
    private Long id;

    @Size(max = 200, message = "成色/瑕疵描述长度不能超过200位")
    private String conditionDesc;

    @Size(max = 100, message = "设备编号长度不能超过100位")
    private String deviceNo;

    @Size(max = 500, message = "备注长度不能超过500位")
    private String remark;
}
