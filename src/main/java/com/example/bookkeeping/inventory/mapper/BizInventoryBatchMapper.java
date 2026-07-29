package com.example.bookkeeping.inventory.mapper;

import com.example.bookkeeping.inventory.entity.BizInventoryBatch;

public interface BizInventoryBatchMapper {
    int insert(BizInventoryBatch batch);

    int deleteByPurchaseId(Long purchaseId);
}
