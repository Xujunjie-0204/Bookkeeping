package com.example.bookkeeping.expense.service;

import com.example.bookkeeping.common.page.PageResult;
import com.example.bookkeeping.expense.dto.ExpenseQueryRequest;
import com.example.bookkeeping.expense.dto.SaveExpenseRequest;
import com.example.bookkeeping.expense.vo.ExpenseSummaryVO;
import com.example.bookkeeping.expense.vo.ExpenseVO;

public interface ExpenseService {
    PageResult<ExpenseVO> page(ExpenseQueryRequest request);

    ExpenseSummaryVO summary(ExpenseQueryRequest request);

    ExpenseVO create(SaveExpenseRequest request);
}
