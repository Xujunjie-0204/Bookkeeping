package com.example.bookkeeping.inventory.mapper;

import com.example.bookkeeping.inventory.entity.BizInventoryBatch;
import com.example.bookkeeping.inventory.vo.InventorySummaryVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface BizInventoryBatchMapper {
    int insert(BizInventoryBatch batch);

    List<InventorySummaryVO> selectSummaryPage(@Param("keyword") String keyword,
                                               @Param("productTypeId") Long productTypeId,
                                               @Param("warningOnly") Boolean warningOnly);

    int updatePurchaseDate(@Param("purchaseId") Long purchaseId, @Param("purchaseDate") LocalDate purchaseDate);

    int deleteByPurchaseId(Long purchaseId);
}
