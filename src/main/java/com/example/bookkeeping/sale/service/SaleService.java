package com.example.bookkeeping.sale.service;

import com.example.bookkeeping.common.page.PageResult;
import com.example.bookkeeping.sale.dto.SaleQueryRequest;
import com.example.bookkeeping.sale.dto.SaveSaleRequest;
import com.example.bookkeeping.sale.dto.UpdateSaleRequest;
import com.example.bookkeeping.sale.vo.SaleItemVO;
import com.example.bookkeeping.sale.vo.SaleStockVO;
import com.example.bookkeeping.sale.vo.SaleSummaryVO;
import com.example.bookkeeping.sale.vo.SaleVO;

import java.util.List;

public interface SaleService {
    PageResult<SaleVO> page(SaleQueryRequest request);

    SaleSummaryVO summary(SaleQueryRequest request);

    List<SaleStockVO> availableStock(String keyword, List<Long> productTypeIds);

    SaleVO create(SaveSaleRequest request);

    SaleVO update(Long id, UpdateSaleRequest request);

    List<SaleItemVO> items(Long saleRecordId);
}
