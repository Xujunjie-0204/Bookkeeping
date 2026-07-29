package com.example.bookkeeping.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Schema(description = "保存商品类型请求")
public class SaveProductTypeRequest {

    @Schema(description = "父类型ID，根节点传0")
    private Long parentId = 0L;

    @NotBlank(message = "类型编码不能为空")
    @Pattern(regexp = "^[A-Za-z0-9_-]{2,50}$", message = "类型编码只能包含字母、数字、下划线和中横线，长度2-50位")
    @Schema(description = "类型编码", required = true)
    private String typeCode;

    @NotBlank(message = "类型名称不能为空")
    @Size(max = 100, message = "类型名称长度不能超过100位")
    @Schema(description = "类型名称", required = true)
    private String typeName;

    @Schema(description = "排序号")
    private Integer sortOrder = 0;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态只能为0或1")
    @Max(value = 1, message = "状态只能为0或1")
    @Schema(description = "状态：1启用，0停用", required = true)
    private Integer status = 1;

    @Size(max = 500, message = "备注长度不能超过500位")
    @Schema(description = "备注")
    private String remark;
}
