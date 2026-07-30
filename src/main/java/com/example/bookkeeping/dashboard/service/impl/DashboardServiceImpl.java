package com.example.bookkeeping.dashboard.service.impl;

import com.example.bookkeeping.dashboard.dto.DashboardQueryRequest;
import com.example.bookkeeping.dashboard.mapper.DashboardMapper;
import com.example.bookkeeping.dashboard.service.DashboardService;
import com.example.bookkeeping.dashboard.vo.DashboardChartVO;
import com.example.bookkeeping.dashboard.vo.DashboardSummaryVO;
import com.example.bookkeeping.dashboard.vo.DashboardTopVO;
import com.example.bookkeeping.dashboard.vo.ExpensePieVO;
import com.example.bookkeeping.dashboard.vo.InventoryWarningVO;
import com.example.bookkeeping.dashboard.vo.ProductTopVO;
import com.example.bookkeeping.dashboard.vo.SalesTrendVO;
import com.example.bookkeeping.dashboard.vo.SuggestionVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {
    private static final BigDecimal QUARTER_TARGET = new BigDecimal("300000.00");
    private static final DateTimeFormatter TREND_DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

    private final DashboardMapper dashboardMapper;

    public DashboardServiceImpl(DashboardMapper dashboardMapper) {
        this.dashboardMapper = dashboardMapper;
    }

    @Override
    public DashboardSummaryVO summary(DashboardQueryRequest request) {
        DashboardQueryRequest query = normalizeRequest(request);
        LocalDate today = LocalDate.now();
        LocalDate quarterStart = currentQuarterStart(today);
        List<Long> productIds = query.getProductIds();
        List<Long> productTypeIds = query.getProductTypeIds();

        BigDecimal todaySale = money(dashboardMapper.selectSaleAmount(today, today, productIds, productTypeIds));
        BigDecimal todayProfit = money(dashboardMapper.selectProfitAmount(today, today, productIds, productTypeIds));
        BigDecimal monthSale = money(dashboardMapper.selectSaleAmount(query.getStartDate(), query.getEndDate(), productIds, productTypeIds));
        BigDecimal monthProfit = money(dashboardMapper.selectProfitAmount(query.getStartDate(), query.getEndDate(), productIds, productTypeIds));
        BigDecimal quarterSale = money(dashboardMapper.selectSaleAmount(quarterStart, today, productIds, productTypeIds));
        BigDecimal quarterProfit = money(dashboardMapper.selectProfitAmount(quarterStart, today, productIds, productTypeIds));

        DashboardSummaryVO vo = new DashboardSummaryVO();
        vo.setTodaySale(todaySale);
        vo.setTodayProfit(todayProfit);
        vo.setMonthSale(monthSale);
        vo.setMonthProfit(monthProfit);
        vo.setQuarterSale(quarterSale);
        vo.setQuarterProfit(quarterProfit);
        Integer inventoryQuantity = dashboardMapper.selectInventoryQuantity(productIds, productTypeIds);
        vo.setInventoryQuantity(inventoryQuantity == null ? 0 : inventoryQuantity);
        vo.setInventoryCost(money(dashboardMapper.selectInventoryCost(productIds, productTypeIds)));
        vo.setQuarterTarget(QUARTER_TARGET);
        vo.setQuarterRemain(QUARTER_TARGET.subtract(quarterSale).max(BigDecimal.ZERO));
        vo.setQuarterPercent(calculatePercent(quarterSale, QUARTER_TARGET));
        return vo;
    }

    @Override
    public DashboardChartVO charts(DashboardQueryRequest request) {
        DashboardQueryRequest query = normalizeRequest(request);
        LocalDate startDate = query.getStartDate();
        LocalDate endDate = query.getEndDate();
        List<Long> productIds = query.getProductIds();
        List<Long> productTypeIds = query.getProductTypeIds();

        DashboardChartVO vo = new DashboardChartVO();
        vo.setSalesTrend(fillTrend(dashboardMapper.selectSalesTrend(startDate, endDate, productIds, productTypeIds), startDate, endDate));
        vo.setProfitTrend(fillTrend(dashboardMapper.selectProfitTrend(startDate, endDate, productIds, productTypeIds), startDate, endDate));
        vo.setExpensePie(dashboardMapper.selectExpensePie(startDate, endDate, productIds, productTypeIds));
        return vo;
    }

    @Override
    public DashboardTopVO top(DashboardQueryRequest request) {
        DashboardQueryRequest query = normalizeRequest(request);
        List<Long> productIds = query.getProductIds();
        List<Long> productTypeIds = query.getProductTypeIds();
        List<ProductTopVO> salesTop = dashboardMapper.selectProductSalesTop(query.getStartDate(), query.getEndDate(), productIds, productTypeIds);
        List<ProductTopVO> profitTop = dashboardMapper.selectProductProfitTop(query.getStartDate(), query.getEndDate(), productIds, productTypeIds);
        List<InventoryWarningVO> warningList = dashboardMapper.selectInventoryWarning(productIds, productTypeIds);

        fillRank(salesTop);
        fillRank(profitTop);

        DashboardTopVO vo = new DashboardTopVO();
        vo.setProductSalesTop(salesTop);
        vo.setProductProfitTop(profitTop);
        vo.setInventoryWarning(warningList);
        vo.setAiSuggestion(buildSuggestions(summary(query), warningList, charts(query).getExpensePie()));
        return vo;
    }

    private DashboardQueryRequest normalizeRequest(DashboardQueryRequest request) {
        DashboardQueryRequest query = request == null ? new DashboardQueryRequest() : request;
        LocalDate today = LocalDate.now();
        if (query.getStartDate() == null) {
            query.setStartDate(today.withDayOfMonth(1));
        }
        if (query.getEndDate() == null) {
            query.setEndDate(today);
        }
        if (query.getStartDate().isAfter(query.getEndDate())) {
            LocalDate startDate = query.getEndDate();
            query.setEndDate(query.getStartDate());
            query.setStartDate(startDate);
        }
        return query;
    }

    private LocalDate currentQuarterStart(LocalDate date) {
        int startMonth = ((date.getMonthValue() - 1) / 3) * 3 + 1;
        return LocalDate.of(date.getYear(), startMonth, 1);
    }

    private BigDecimal money(BigDecimal value) {
        return (value == null ? BigDecimal.ZERO : value).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculatePercent(BigDecimal amount, BigDecimal target) {
        if (target == null || target.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return amount.multiply(new BigDecimal("100")).divide(target, 2, RoundingMode.HALF_UP);
    }

    private List<SalesTrendVO> fillTrend(List<SalesTrendVO> source, LocalDate startDate, LocalDate endDate) {
        List<SalesTrendVO> result = new ArrayList<SalesTrendVO>();
        LocalDate cursor = startDate;
        while (!cursor.isAfter(endDate)) {
            String dateText = cursor.format(TREND_DATE_FORMATTER);
            SalesTrendVO item = findTrend(source, dateText);
            if (item == null) {
                item = new SalesTrendVO();
                item.setDate(dateText);
                item.setAmount(BigDecimal.ZERO);
            } else {
                item.setAmount(money(item.getAmount()));
            }
            result.add(item);
            cursor = cursor.plusDays(1);
        }
        return result;
    }

    private SalesTrendVO findTrend(List<SalesTrendVO> source, String dateText) {
        if (source == null) {
            return null;
        }
        for (SalesTrendVO item : source) {
            if (dateText.equals(item.getDate())) {
                return item;
            }
        }
        return null;
    }

    private void fillRank(List<ProductTopVO> list) {
        if (list == null) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            ProductTopVO item = list.get(i);
            item.setRankNo(i + 1);
            item.setSaleAmount(money(item.getSaleAmount()));
            item.setProfitAmount(money(item.getProfitAmount()));
            item.setProfitRate(money(item.getProfitRate()));
        }
    }

    private List<SuggestionVO> buildSuggestions(DashboardSummaryVO summary,
                                                List<InventoryWarningVO> warningList,
                                                List<ExpensePieVO> expensePie) {
        List<SuggestionVO> list = new ArrayList<SuggestionVO>();
        int sortNo = 1;
        if (warningList != null && !warningList.isEmpty()) {
            list.add(suggestion(sortNo++, "存在库存预警商品，建议优先检查补货或调整销售节奏。", "high"));
        }
        if (summary.getQuarterPercent().compareTo(new BigDecimal("80")) >= 0) {
            list.add(suggestion(sortNo++, "季度销售额接近经营目标，建议关注开票、费用归集和现金流安排。", "medium"));
        }
        if (hasExpense(expensePie, "快递费")) {
            list.add(suggestion(sortNo++, "本期存在快递费用，建议持续关注物流成本变化。", "medium"));
        }
        list.add(suggestion(sortNo++, "建议定期复盘高利润商品，优先投入到周转快、毛利稳的品类。", "low"));
        return list;
    }

    private boolean hasExpense(List<ExpensePieVO> expensePie, String name) {
        if (expensePie == null) {
            return false;
        }
        for (ExpensePieVO item : expensePie) {
            if (name.equals(item.getName()) && item.getValue() != null && item.getValue().compareTo(BigDecimal.ZERO) > 0) {
                return true;
            }
        }
        return false;
    }

    private SuggestionVO suggestion(Integer sortNo, String content, String riskLevel) {
        SuggestionVO vo = new SuggestionVO();
        vo.setSortNo(sortNo);
        vo.setContent(content);
        vo.setRiskLevel(riskLevel);
        return vo;
    }
}
