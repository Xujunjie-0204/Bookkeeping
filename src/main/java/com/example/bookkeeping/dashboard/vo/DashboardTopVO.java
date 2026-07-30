package com.example.bookkeeping.dashboard.vo;

import lombok.Data;

import java.util.List;

@Data
public class DashboardTopVO {
    private List<ProductTopVO> productSalesTop;
    private List<ProductTopVO> productProfitTop;
    private List<InventoryWarningVO> inventoryWarning;
    private List<SuggestionVO> aiSuggestion;
}
