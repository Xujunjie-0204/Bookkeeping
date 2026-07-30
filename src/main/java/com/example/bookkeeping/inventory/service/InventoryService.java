package com.example.bookkeeping.inventory.service;

import com.example.bookkeeping.common.page.PageResult;
import com.example.bookkeeping.inventory.dto.InventoryQueryRequest;
import com.example.bookkeeping.inventory.vo.InventorySummaryVO;

public interface InventoryService {
    PageResult<InventorySummaryVO> page(InventoryQueryRequest request);
}
