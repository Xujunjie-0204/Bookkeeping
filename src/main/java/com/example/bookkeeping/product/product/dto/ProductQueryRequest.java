package com.example.bookkeeping.product.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "商品查询条件")
public class ProductQueryRequest {

    @Schema(description = "页码")
    private Integer pageNum = 1;

    @Schema(description = "每页条数")
    private Integer pageSize = 10;

    @Schema(description = "商品编码或名称关键字")
    private String keyword;

    @Schema(description = "商品类型ID")
    private Long productTypeId;

    @Schema(description = "状态：1启用，0停用")
    private Integer status;
}
