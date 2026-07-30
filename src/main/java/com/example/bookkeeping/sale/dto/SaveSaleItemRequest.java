package com.example.bookkeeping.sale.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class SaveSaleItemRequest {
    @NotNull(message = "请选择进货商品")
    private Long batchId;

    @NotNull(message = "销售数量不能为空")
    @Min(value = 1, message = "销售数量至少为1")
    private Integer quantity;

    @NotNull(message = "销售单价不能为空")
    @DecimalMin(value = "0.00", message = "销售单价不能小于0")
    private BigDecimal saleUnitPrice;

    private String remark;
}
