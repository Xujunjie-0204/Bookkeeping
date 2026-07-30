package com.example.bookkeeping.controller;

import com.example.bookkeeping.common.api.ApiResult;
import com.example.bookkeeping.dashboard.dto.DashboardQueryRequest;
import com.example.bookkeeping.dashboard.service.DashboardService;
import com.example.bookkeeping.dashboard.vo.DashboardChartVO;
import com.example.bookkeeping.dashboard.vo.DashboardSummaryVO;
import com.example.bookkeeping.dashboard.vo.DashboardTopVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ApiResult<DashboardSummaryVO> summary(DashboardQueryRequest request) {
        return ApiResult.success(dashboardService.summary(request));
    }

    @GetMapping("/charts")
    public ApiResult<DashboardChartVO> charts(DashboardQueryRequest request) {
        return ApiResult.success(dashboardService.charts(request));
    }

    @GetMapping("/top")
    public ApiResult<DashboardTopVO> top(DashboardQueryRequest request) {
        return ApiResult.success(dashboardService.top(request));
    }
}
