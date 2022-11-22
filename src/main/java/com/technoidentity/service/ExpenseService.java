package com.technoidentity.service;

import com.technoidentity.entity.Expense;
import com.technoidentity.exception.CashFlowException;
import com.technoidentity.request.ExpenseRequest;
import com.technoidentity.util.Pagination;
import org.springframework.data.domain.Pageable;

public interface ExpenseService {

  Expense addExpense(ExpenseRequest expenseRequest, Long userId) throws CashFlowException;

  Pagination getAllExpenses(Pageable pageable);

  Expense updateExpense(String id, ExpenseRequest expenseRequest);

  void deleteExpenseById(String id);
}
