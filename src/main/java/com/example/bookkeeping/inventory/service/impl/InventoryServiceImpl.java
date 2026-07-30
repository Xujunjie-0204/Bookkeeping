package com.example.bookkeeping.inventory.service.impl;

import com.example.bookkeeping.common.page.PageResult;
import com.example.bookkeeping.inventory.dto.InventoryQueryRequest;
import com.example.bookkeeping.inventory.mapper.BizInventoryBatchMapper;
import com.example.bookkeeping.inventory.service.InventoryService;
import com.example.bookkeeping.inventory.vo.InventorySummaryVO;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final BizInventoryBatchMapper inventoryBatchMapper;

    public InventoryServiceImpl(BizInventoryBatchMapper inventoryBatchMapper) {
        this.inventoryBatchMapper = inventoryBatchMapper;
    }

    @Override
    public PageResult<InventorySummaryVO> page(InventoryQueryRequest request) {
        int pageNum = request.getPageNum() == null || request.getPageNum() < 1 ? 1 : request.getPageNum();
        int pageSize = request.getPageSize() == null || request.getPageSize() < 1 ? 10 : request.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        return PageResult.of(inventoryBatchMapper.selectSummaryPage(
                StringUtils.hasText(request.getKeyword()) ? request.getKeyword().trim() : null,
                request.getProductTypeId(),
                Boolean.TRUE.equals(request.getWarningOnly())
        ));
    }
}
