package com.example.bookkeeping.dashboard.vo;

import lombok.Data;

@Data
public class InventoryWarningVO {
    private String productName;
    private Integer inventoryQuantity;
    private Integer warningValue;
    private String status;
}
