package com.example.bookkeeping.expense.mapper;

import com.example.bookkeeping.expense.entity.BizExpense;
import com.example.bookkeeping.expense.vo.ExpenseSummaryVO;
import com.example.bookkeeping.expense.vo.ExpenseVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface BizExpenseMapper {
    List<ExpenseVO> selectPage(@Param("keyword") String keyword,
                               @Param("expenseType") String expenseType,
                               @Param("startDate") LocalDate startDate,
                               @Param("endDate") LocalDate endDate);

    ExpenseSummaryVO selectSummary(@Param("keyword") String keyword,
                                   @Param("expenseType") String expenseType,
                                   @Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate);

    int insert(BizExpense expense);
}
