package com.example.bookkeeping.product.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "商品信息")
public class ProductVO {

    private Long id;
    private String productCode;
    private String productName;
    private Long productTypeId;
    private String productTypeName;
    private String categoryName;
    private String brand;
    private String model;
    private String specification;
    private BigDecimal defaultCost;
    private BigDecimal defaultSalePrice;
    private Integer warningStock;
    private Integer status;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
