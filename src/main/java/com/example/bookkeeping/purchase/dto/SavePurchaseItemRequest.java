package com.example.bookkeeping.purchase.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Schema(description = "采购明细请求")
public class SavePurchaseItemRequest {
    @NotNull(message = "商品不能为空")
    private Long productId;

    @Size(max = 200, message = "成色/瑕疵描述长度不能超过200位")
    private String conditionDesc;

    @Size(max = 100, message = "设备编号长度不能超过100位")
    private String deviceNo;

    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量不能小于1")
    private Integer quantity = 1;

    @NotNull(message = "单价不能为空")
    @DecimalMin(value = "0.00", message = "单价不能小于0")
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Size(max = 500, message = "备注长度不能超过500位")
    private String remark;
}
