package com.example.bookkeeping.expense.service.impl;

import com.example.bookkeeping.common.page.PageResult;
import com.example.bookkeeping.expense.dto.ExpenseQueryRequest;
import com.example.bookkeeping.expense.dto.SaveExpenseRequest;
import com.example.bookkeeping.expense.entity.BizExpense;
import com.example.bookkeeping.expense.mapper.BizExpenseMapper;
import com.example.bookkeeping.expense.service.ExpenseService;
import com.example.bookkeeping.expense.vo.ExpenseSummaryVO;
import com.example.bookkeeping.expense.vo.ExpenseVO;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final BizExpenseMapper expenseMapper;

    public ExpenseServiceImpl(BizExpenseMapper expenseMapper) {
        this.expenseMapper = expenseMapper;
    }

    @Override
    public PageResult<ExpenseVO> page(ExpenseQueryRequest request) {
        int pageNum = request.getPageNum() == null || request.getPageNum() < 1 ? 1 : request.getPageNum();
        int pageSize = request.getPageSize() == null || request.getPageSize() < 1 ? 10 : request.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        return PageResult.of(expenseMapper.selectPage(
                trimToNull(request.getKeyword()),
                trimToNull(request.getExpenseType()),
                request.getStartDate(),
                request.getEndDate()
        ));
    }

    @Override
    public ExpenseSummaryVO summary(ExpenseQueryRequest request) {
        return expenseMapper.selectSummary(
                trimToNull(request.getKeyword()),
                trimToNull(request.getExpenseType()),
                request.getStartDate(),
                request.getEndDate()
        );
    }

    @Override
    public ExpenseVO create(SaveExpenseRequest request) {
        BizExpense expense = new BizExpense();
        expense.setExpenseNo(generateExpenseNo());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setExpenseType(request.getExpenseType().trim());
        expense.setExpenseName(request.getExpenseName().trim());
        expense.setAmount(request.getAmount());
        expense.setExpressCompany(trimToNull(request.getExpressCompany()));
        expense.setShipmentCount(request.getShipmentCount());
        expense.setIndependentFlag(1);
        expense.setPaymentMethod(trimToNull(request.getPaymentMethod()));
        expense.setVoucherNo(trimToNull(request.getVoucherNo()));
        expense.setRemark(trimToNull(request.getRemark()));
        expenseMapper.insert(expense);

        List<ExpenseVO> list = expenseMapper.selectPage(expense.getExpenseNo(), null, null, null);
        return list.isEmpty() ? null : list.get(0);
    }

    private String generateExpenseNo() {
        return "E" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
                + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    private String trimToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
}
