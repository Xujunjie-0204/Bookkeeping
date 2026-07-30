package com.example.bookkeeping.expense.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExpenseSummaryVO {
    private Integer expenseCount;
    private BigDecimal totalExpenseAmount;
}
