package com.example.bookkeeping.product.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BizProductType {

    private Long id;
    private String typeCode;
    private String typeName;
    private Long parentId;
    private Integer sortOrder;
    private Integer status;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleted;
}
