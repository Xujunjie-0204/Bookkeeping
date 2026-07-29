package com.example.bookkeeping.purchase.mapper;

import com.example.bookkeeping.purchase.entity.BizPurchaseItem;

public interface BizPurchaseItemMapper {
    int insert(BizPurchaseItem item);

    int deleteByPurchaseId(Long purchaseId);
}
