package com.example.bookkeeping.inventory.dto;

import lombok.Data;

@Data
public class InventoryQueryRequest {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String keyword;
    private Long productTypeId;
    private Boolean warningOnly;
}
