package com.example.bookkeeping.purchase.mapper;

import com.example.bookkeeping.purchase.entity.BizPurchaseItem;
import com.example.bookkeeping.purchase.vo.PurchaseItemVO;

import java.util.List;

public interface BizPurchaseItemMapper {
    int insert(BizPurchaseItem item);

    List<PurchaseItemVO> selectVOByPurchaseId(Long purchaseId);

    int updateEditableById(BizPurchaseItem item);

    int deleteByPurchaseId(Long purchaseId);
}
