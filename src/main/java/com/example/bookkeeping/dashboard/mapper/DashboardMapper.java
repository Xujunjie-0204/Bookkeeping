package com.example.bookkeeping.dashboard.mapper;

import com.example.bookkeeping.dashboard.vo.ExpensePieVO;
import com.example.bookkeeping.dashboard.vo.InventoryWarningVO;
import com.example.bookkeeping.dashboard.vo.ProductTopVO;
import com.example.bookkeeping.dashboard.vo.SalesTrendVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface DashboardMapper {
    BigDecimal selectSaleAmount(@Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate,
                                @Param("productIds") List<Long> productIds,
                                @Param("productTypeIds") List<Long> productTypeIds);

    BigDecimal selectProfitAmount(@Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate,
                                  @Param("productIds") List<Long> productIds,
                                  @Param("productTypeIds") List<Long> productTypeIds);

    Integer selectInventoryQuantity(@Param("productIds") List<Long> productIds,
                                    @Param("productTypeIds") List<Long> productTypeIds);

    BigDecimal selectInventoryCost(@Param("productIds") List<Long> productIds,
                                   @Param("productTypeIds") List<Long> productTypeIds);

    List<SalesTrendVO> selectSalesTrend(@Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate,
                                        @Param("productIds") List<Long> productIds,
                                        @Param("productTypeIds") List<Long> productTypeIds);

    List<SalesTrendVO> selectProfitTrend(@Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate,
                                         @Param("productIds") List<Long> productIds,
                                         @Param("productTypeIds") List<Long> productTypeIds);

    List<ExpensePieVO> selectExpensePie(@Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate,
                                        @Param("productIds") List<Long> productIds,
                                        @Param("productTypeIds") List<Long> productTypeIds);

    List<ProductTopVO> selectProductSalesTop(@Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate,
                                             @Param("productIds") List<Long> productIds,
                                             @Param("productTypeIds") List<Long> productTypeIds);

    List<ProductTopVO> selectProductProfitTop(@Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate,
                                              @Param("productIds") List<Long> productIds,
                                              @Param("productTypeIds") List<Long> productTypeIds);

    List<InventoryWarningVO> selectInventoryWarning(@Param("productIds") List<Long> productIds,
                                                    @Param("productTypeIds") List<Long> productTypeIds);
}
