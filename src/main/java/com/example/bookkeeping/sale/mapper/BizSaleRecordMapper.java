package com.example.bookkeeping.sale.mapper;

import com.example.bookkeeping.sale.entity.BizSaleRecord;
import com.example.bookkeeping.sale.vo.SaleSummaryVO;
import com.example.bookkeeping.sale.vo.SaleVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

public interface BizSaleRecordMapper {
    BizSaleRecord selectById(@Param("id") Long id);

    BizSaleRecord selectByRecordNo(@Param("recordNo") String recordNo);

    SaleVO selectVOById(@Param("id") Long id);

    List<SaleVO> selectPage(@Param("keyword") String keyword,
                            @Param("paymentStatus") Integer paymentStatus,
                            @Param("shipmentStatus") Integer shipmentStatus,
                            @Param("startDate") LocalDate startDate,
                            @Param("endDate") LocalDate endDate);

    SaleSummaryVO selectSummary(@Param("keyword") String keyword,
                                @Param("paymentStatus") Integer paymentStatus,
                                @Param("shipmentStatus") Integer shipmentStatus,
                                @Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate);

    int insert(BizSaleRecord record);

    int updateTotals(@Param("id") Long id,
                     @Param("totalSaleAmount") BigDecimal totalSaleAmount,
                     @Param("totalCostAmount") BigDecimal totalCostAmount,
                     @Param("receivedAmount") BigDecimal receivedAmount,
                     @Param("profitAmount") BigDecimal profitAmount);

    int updateEditableById(BizSaleRecord record);
}
