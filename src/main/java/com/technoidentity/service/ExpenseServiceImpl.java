package com.technoidentity.service;

import com.technoidentity.entity.Expense;
import com.technoidentity.entity.Ledger;
import com.technoidentity.exception.CashFlowException;
import com.technoidentity.repository.ExpenseRepository;
import com.technoidentity.repository.LedgerRepository;
import com.technoidentity.request.ExpenseRequest;
import com.technoidentity.response.ExpenseResponse;
import com.technoidentity.util.Pagination;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseServiceImpl implements ExpenseService {
  @Autowired final Mapper mapper;
  @Autowired private final ExpenseRepository expenseRepository;
  @Autowired private final LedgerRepository ledgerRepository;
  @Autowired private final CashFlowService cashFlowService;

  @Override
  @Transactional(rollbackFor = CashFlowException.class)
  public Expense addExpense(ExpenseRequest expenseRequest, Long userId) throws CashFlowException {

    Expense expense = new Expense();
    expense.setExpenseType(expenseRequest.getExpenseType());
    expense.setRecurringExpense(expenseRequest.getRecurringExpense());
    expense.setAmount(expenseRequest.getAmount());
    expense.setBankName(expenseRequest.getBankName());
    expense.setRepeatExpenseType(expenseRequest.getRepeatExpenseType());
    expense.setDebitChequeNo(expenseRequest.getDebitChequeNo());
    expense.setRecipient(expenseRequest.getRecipient());
    expense.setRepeatInstance(expenseRequest.getRepeatInstance());
    expense.setTransactionDate(expenseRequest.getTransactionDate());
    expense.setTransactionDescription(expenseRequest.getTransactionDescription());
    expense.setTransactionId(expenseRequest.getTransactionId());
    expense.setCreatedBy(userId);
    expense.setCreatedAt(new Date());
    expense.setUpdatedBy(userId);
    expense.setUpdatedAt(new Date());
    expense.setStatus(1);
    Expense result;

    // save in ledger
    // saveExpenseAsLedger(result);

    // update cashflow
    try {
      result = expenseRepository.save(expense);
      cashFlowService.saveOrUpdateCashFlow(result.getTransactionDate(), result.getAmount(), null);
    } catch (Exception e) {
      log.error("Could not create expense and cashflow, error is " + e.getMessage());
      throw new CashFlowException(
          "could not create expense and cashflow error is " + e.getMessage());
    }
    /*CashFlow cashFlow = cashFlowRepository.findByDate(expense.getTransactionDate());
    if (cashFlow != null) {
      cashFlow.setOutFlow(cashFlow.getOutFlow() + expense.getAmount());

      cashFlowRepository.save(cashFlow);
    }*/
    return result;
  }

  public void saveExpenseAsLedger(Expense expense) {
    Ledger ledger = new Ledger();
    ledger.setCapitalExpenseOrRevenueId(expense.getExpenseId());
    ledger.setExpenseOrRevenueType(expense.getExpenseType());
    ledger.setRecurring(expense.getRecurringExpense());
    ledger.setAmount(expense.getAmount());
    ledger.setBankName(expense.getBankName());
    ledger.setRepeatType(expense.getRepeatExpenseType());
    ledger.setChequeNo(expense.getDebitChequeNo());
    ledger.setRecipientOrPayee(expense.getRecipient());
    ledger.setRepeatInstance(expense.getRepeatInstance());
    ledger.setDate(expense.getTransactionDate());
    ledger.setTransactionDescription(expense.getTransactionDescription());
    ledger.setTransactionId(expense.getTransactionId());
    ledgerRepository.save(ledger);
  }

  @Override
  public Pagination getAllExpenses(Pageable pageable) {
    Page<Expense> expense = expenseRepository.findAll(pageable);

    Long total = expense.getTotalElements();
    List<ExpenseResponse> expenseList =
        expense
            .stream()
            .map(
                c -> {
                  ExpenseResponse expenseResponse = mapper.map(c, ExpenseResponse.class);
                  return expenseResponse;
                })
            .collect(Collectors.toList());

    return new Pagination(expenseList, total.intValue());
  }

  @Override
  public Expense updateExpense(String id, ExpenseRequest expenseRequest) {

    Expense expense;
    try {
      expense = expenseRepository.getOne(id);
      expense.setExpenseType(expenseRequest.getExpenseType());
      expense.setRecurringExpense(expenseRequest.getRecurringExpense());
      expense.setRepeatExpenseType(expenseRequest.getRepeatExpenseType());
      expense.setRecipient(expenseRequest.getRecipient());
      expense.setTransactionId(expenseRequest.getTransactionId());
      expense.setTransactionDate(expenseRequest.getTransactionDate());
      expense.setTransactionDescription(expenseRequest.getTransactionDescription());
      expense.setRepeatInstance(expenseRequest.getRepeatInstance());
      expense.setDebitChequeNo(expenseRequest.getDebitChequeNo());
      expense.setAmount(expenseRequest.getAmount());
      expense.setBankName(expenseRequest.getBankName());
      return expenseRepository.save(expense);
    } catch (Exception e) {
      log.error("unable to update expense error is {} ", e.getMessage());
      return null;
    }
  }

  @Override
  public void deleteExpenseById(String id) {
    expenseRepository.deleteById(id);
  }
}
