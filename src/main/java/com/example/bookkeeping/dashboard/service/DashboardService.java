package com.example.bookkeeping.dashboard.service;

import com.example.bookkeeping.dashboard.dto.DashboardQueryRequest;
import com.example.bookkeeping.dashboard.vo.DashboardChartVO;
import com.example.bookkeeping.dashboard.vo.DashboardSummaryVO;
import com.example.bookkeeping.dashboard.vo.DashboardTopVO;

public interface DashboardService {
    DashboardSummaryVO summary(DashboardQueryRequest request);

    DashboardChartVO charts(DashboardQueryRequest request);

    DashboardTopVO top(DashboardQueryRequest request);
}
