package com.example.bookkeeping.controller;

import com.example.bookkeeping.common.api.ApiResult;
import com.example.bookkeeping.common.page.PageResult;
import com.example.bookkeeping.expense.dto.ExpenseQueryRequest;
import com.example.bookkeeping.expense.dto.SaveExpenseRequest;
import com.example.bookkeeping.expense.service.ExpenseService;
import com.example.bookkeeping.expense.vo.ExpenseSummaryVO;
import com.example.bookkeeping.expense.vo.ExpenseVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ApiResult<PageResult<ExpenseVO>> page(@ModelAttribute ExpenseQueryRequest request) {
        return ApiResult.success(expenseService.page(request));
    }

    @GetMapping("/summary")
    public ApiResult<ExpenseSummaryVO> summary(@ModelAttribute ExpenseQueryRequest request) {
        return ApiResult.success(expenseService.summary(request));
    }

    @PostMapping
    public ApiResult<ExpenseVO> create(@Valid @RequestBody SaveExpenseRequest request) {
        return ApiResult.success(expenseService.create(request));
    }
}
