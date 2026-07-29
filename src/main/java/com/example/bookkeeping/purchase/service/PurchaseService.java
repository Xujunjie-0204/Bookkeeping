package com.example.bookkeeping.purchase.service;

import com.example.bookkeeping.common.page.PageResult;
import com.example.bookkeeping.purchase.dto.PurchaseQueryRequest;
import com.example.bookkeeping.purchase.dto.SavePurchaseRequest;
import com.example.bookkeeping.purchase.vo.PurchaseVO;

public interface PurchaseService {
    PageResult<PurchaseVO> page(PurchaseQueryRequest request);

    PurchaseVO create(SavePurchaseRequest request);

    void delete(Long id);
}
