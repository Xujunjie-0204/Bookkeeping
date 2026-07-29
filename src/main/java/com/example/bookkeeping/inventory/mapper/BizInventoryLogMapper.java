package com.example.bookkeeping.inventory.mapper;

import com.example.bookkeeping.inventory.entity.BizInventoryLog;

public interface BizInventoryLogMapper {
    int insert(BizInventoryLog log);

    int deletePurchaseInLogs(Long purchaseId);
}
