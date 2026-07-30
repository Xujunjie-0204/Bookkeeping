package com.example.bookkeeping.purchase.service;

import com.example.bookkeeping.common.page.PageResult;
import com.example.bookkeeping.purchase.dto.PurchaseQueryRequest;
import com.example.bookkeeping.purchase.dto.SavePurchaseRequest;
import com.example.bookkeeping.purchase.dto.UpdatePurchaseRequest;
import com.example.bookkeeping.purchase.vo.PurchaseItemVO;
import com.example.bookkeeping.purchase.vo.PurchaseSummaryVO;
import com.example.bookkeeping.purchase.vo.PurchaseVO;

import java.util.List;

public interface PurchaseService {
    PageResult<PurchaseVO> page(PurchaseQueryRequest request);

    PurchaseSummaryVO summary(PurchaseQueryRequest request);

    PurchaseVO create(SavePurchaseRequest request);

    PurchaseVO update(Long id, UpdatePurchaseRequest request);

    List<PurchaseItemVO> items(Long id);

    void delete(Long id);
}
