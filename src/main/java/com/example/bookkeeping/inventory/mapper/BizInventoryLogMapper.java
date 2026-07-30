package com.example.bookkeeping.inventory.mapper;

import com.example.bookkeeping.inventory.entity.BizInventoryLog;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

public interface BizInventoryLogMapper {
    int insert(BizInventoryLog log);

    int updatePurchaseInBusinessDate(@Param("purchaseId") Long purchaseId, @Param("businessDate") LocalDate businessDate);

    int updateSaleOutBusinessDate(@Param("saleRecordId") Long saleRecordId, @Param("businessDate") LocalDate businessDate);

    int deletePurchaseInLogs(Long purchaseId);
}
