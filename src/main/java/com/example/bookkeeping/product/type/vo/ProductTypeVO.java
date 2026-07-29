package com.example.bookkeeping.product.type.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "商品类型信息")
public class ProductTypeVO {

    private Long id;
    private String typeCode;
    private String typeName;
    private Long parentId;
    private Integer sortOrder;
    private Integer status;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProductTypeVO> children = new ArrayList<ProductTypeVO>();
}
