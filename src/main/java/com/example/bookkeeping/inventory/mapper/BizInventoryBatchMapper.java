package com.example.bookkeeping.inventory.mapper;

import com.example.bookkeeping.inventory.entity.BizInventoryBatch;
import com.example.bookkeeping.inventory.vo.InventorySummaryVO;
import com.example.bookkeeping.sale.vo.SaleStockVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BizInventoryBatchMapper {
    int insert(BizInventoryBatch batch);

    List<InventorySummaryVO> selectSummaryPage(@Param("keyword") String keyword,
                                               @Param("productTypeId") Long productTypeId,
                                               @Param("productTypeIds") List<Long> productTypeIds,
                                               @Param("warningOnly") Boolean warningOnly);

    InventorySummaryVO selectTotalSummary(@Param("keyword") String keyword,
                                          @Param("productTypeId") Long productTypeId,
                                          @Param("productTypeIds") List<Long> productTypeIds,
                                          @Param("warningOnly") Boolean warningOnly);

    List<SaleStockVO> selectAvailableForSale(@Param("keyword") String keyword,
                                             @Param("productTypeIds") List<Long> productTypeIds);

    BizInventoryBatch selectForUpdate(@Param("id") Long id);

    int decreaseForSale(@Param("id") Long id, @Param("quantity") Integer quantity);

    int updatePurchaseDate(@Param("purchaseId") Long purchaseId, @Param("purchaseDate") LocalDateTime purchaseDate);

    int deleteByPurchaseId(Long purchaseId);
}
