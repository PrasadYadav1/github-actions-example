package com.technoidentity.service;

import com.technoidentity.dto.CashFlowDto;
import com.technoidentity.entity.CashFlow;
import com.technoidentity.request.CashFlowRequest;
import com.technoidentity.request.FirstCashFlowRequest;
import com.technoidentity.util.Pagination;
import java.text.ParseException;
import org.springframework.data.domain.Pageable;

public interface CashFlowService {

  void saveOrUpdateCashFlow(String transactionDate, Double expenseAmount, Double revenueAmount)
      throws Exception;

  CashFlow addFirstCashflow(FirstCashFlowRequest firstCashFlowRequest) throws ParseException;

  CashFlowDto getCashFlowById(String id);

  Pagination getAllCashFlow(Pageable pageable);

  CashFlow updateCashFlowById(String id, CashFlowRequest cashFlowRequest);

  void deleteCashFlowById(String id);
}
