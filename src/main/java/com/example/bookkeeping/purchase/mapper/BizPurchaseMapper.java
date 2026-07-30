package com.example.bookkeeping.purchase.mapper;

import com.example.bookkeeping.purchase.entity.BizPurchase;
import com.example.bookkeeping.purchase.vo.PurchaseSummaryVO;
import com.example.bookkeeping.purchase.vo.PurchaseVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface BizPurchaseMapper {
    BizPurchase selectById(@Param("id") Long id);

    BizPurchase selectByPurchaseNo(@Param("purchaseNo") String purchaseNo);

    List<PurchaseVO> selectPage(@Param("keyword") String keyword,
                                @Param("purchaseStatus") Integer purchaseStatus,
                                @Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate,
                                @Param("productIds") List<Long> productIds);

    PurchaseSummaryVO selectSummary(@Param("keyword") String keyword,
                                    @Param("purchaseStatus") Integer purchaseStatus,
                                    @Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate,
                                    @Param("productIds") List<Long> productIds);

    int insert(BizPurchase purchase);

    int updateEditableById(BizPurchase purchase);

    int refreshSaleStatus(@Param("id") Long id);

    int deleteById(@Param("id") Long id);
}
