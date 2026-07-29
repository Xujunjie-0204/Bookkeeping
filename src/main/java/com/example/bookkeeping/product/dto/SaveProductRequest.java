package com.example.bookkeeping.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Schema(description = "保存商品请求")
public class SaveProductRequest {

    @NotBlank(message = "商品编码不能为空")
    @Size(max = 50, message = "商品编码长度不能超过50位")
    @Schema(description = "商品编码", required = true)
    private String productCode;

    @NotBlank(message = "商品名称不能为空")
    @Size(max = 100, message = "商品名称长度不能超过100位")
    @Schema(description = "商品名称", required = true)
    private String productName;

    @Schema(description = "商品类型ID")
    private Long productTypeId;

    @Size(max = 50, message = "品牌长度不能超过50位")
    @Schema(description = "品牌")
    private String brand;

    @Size(max = 80, message = "型号长度不能超过80位")
    @Schema(description = "型号")
    private String model;

    @Size(max = 200, message = "规格长度不能超过200位")
    @Schema(description = "规格")
    private String specification;

    @NotNull(message = "默认成本价不能为空")
    @DecimalMin(value = "0.00", message = "默认成本价不能小于0")
    @Schema(description = "默认成本价", required = true)
    private BigDecimal defaultCost = BigDecimal.ZERO;

    @NotNull(message = "默认销售价不能为空")
    @DecimalMin(value = "0.00", message = "默认销售价不能小于0")
    @Schema(description = "默认销售价", required = true)
    private BigDecimal defaultSalePrice = BigDecimal.ZERO;

    @NotNull(message = "库存预警数量不能为空")
    @Min(value = 0, message = "库存预警数量不能小于0")
    @Schema(description = "库存预警数量", required = true)
    private Integer warningStock = 0;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态只能为0或1")
    @Max(value = 1, message = "状态只能为0或1")
    @Schema(description = "状态：1启用，0停用", required = true)
    private Integer status = 1;

    @Size(max = 500, message = "备注长度不能超过500位")
    @Schema(description = "备注")
    private String remark;
}
