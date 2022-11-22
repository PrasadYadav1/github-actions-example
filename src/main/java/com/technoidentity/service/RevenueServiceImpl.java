package com.technoidentity.service;

import com.technoidentity.entity.Ledger;
import com.technoidentity.entity.Revenue;
import com.technoidentity.exception.CashFlowException;
import com.technoidentity.repository.LedgerRepository;
import com.technoidentity.repository.RevenueRepository;
import com.technoidentity.request.RevenueRequest;
import com.technoidentity.response.RevenueResponse;
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
public class RevenueServiceImpl implements RevenueService {

  @Autowired private final Mapper mapper;

  @Autowired private final RevenueRepository revenueRepository;

  @Autowired private final LedgerRepository ledgerRepository;

  @Autowired private final CashFlowService cashFlowService;

  @Override
  @Transactional(rollbackFor = CashFlowException.class)
  public Revenue addRevenue(RevenueRequest revenueRequest, Long userId) throws CashFlowException {
    Revenue revenue = new Revenue();

    revenue.setRevenueType(revenueRequest.getRevenueType());
    revenue.setRecurringRevenue(revenueRequest.getRecurringRevenue());
    revenue.setAmount(revenueRequest.getAmount());
    revenue.setBankName(revenueRequest.getBankName());
    revenue.setRepeatRevenueType(revenueRequest.getRepeatRevenueType());
    revenue.setCreditChequeNo(revenueRequest.getCreditChequeNo());
    revenue.setPayer(revenueRequest.getPayer());
    revenue.setRepeatInstance(revenueRequest.getRepeatInstance());
    revenue.setTransactionDate(revenueRequest.getTransactionDate());
    revenue.setTransactionDescription(revenueRequest.getTransactionDescription());
    revenue.setTransactionId(revenueRequest.getTransactionId());
    revenue.setCreatedBy(userId);
    revenue.setCreatedAt(new Date());
    revenue.setUpdatedBy(userId);
    revenue.setUpdatedAt(new Date());
    revenue.setStatus(1);
    Revenue result;
    // save in legder
    //  saveRevenueAsLedger(result);

    try {
      result = revenueRepository.save(revenue);
      cashFlowService.saveOrUpdateCashFlow(result.getTransactionDate(), null, result.getAmount());
    } catch (Exception e) {
      log.error("Could not create revenue and cashflow, error is " + e.getMessage());
      throw new CashFlowException(
          "could not create revenue and cashflow error is " + e.getMessage());
    }
    return result;
  }

  public void saveRevenueAsLedger(Revenue revenue) {
    Ledger ledger = new Ledger();
    ledger.setCapitalExpenseOrRevenueId(revenue.getRevenueId());
    ledger.setExpenseOrRevenueType(revenue.getRevenueType());
    ledger.setRecurring(revenue.getRecurringRevenue());
    ledger.setAmount(revenue.getAmount());
    ledger.setBankName(revenue.getBankName());
    ledger.setRepeatType(revenue.getRepeatRevenueType());
    ledger.setChequeNo(revenue.getCreditChequeNo());
    ledger.setRecipientOrPayee(revenue.getPayer());
    ledger.setRepeatInstance(revenue.getRepeatInstance());
    ledger.setDate(revenue.getTransactionDate());
    ledger.setTransactionDescription(revenue.getTransactionDescription());
    ledger.setTransactionId(revenue.getTransactionId());
    ledgerRepository.save(ledger);
  }

  @Override
  public Pagination getAllRevenues(Pageable pageable) {

    Page<Revenue> revenue = revenueRepository.findAll(pageable);

    Long total = revenue.getTotalElements();
    List<RevenueResponse> revenueList =
        revenue
            .stream()
            .map(
                c -> {
                  RevenueResponse revenueResponse = mapper.map(c, RevenueResponse.class);
                  return revenueResponse;
                })
            .collect(Collectors.toList());

    return new Pagination(revenueList, total.intValue());
  }

  @Override
  public Revenue updateRevenue(String id, RevenueRequest revenueRequest) {
    Revenue revenue;
    try {
      revenue = revenueRepository.getOne(id);
      revenue.setRevenueType(revenueRequest.getRevenueType());
      revenue.setRecurringRevenue(revenueRequest.getRecurringRevenue());
      revenue.setRepeatRevenueType(revenueRequest.getRepeatRevenueType());
      revenue.setPayer(revenueRequest.getPayer());
      revenue.setTransactionId(revenueRequest.getTransactionId());
      revenue.setTransactionDate(revenueRequest.getTransactionDate());
      revenue.setTransactionDescription(revenueRequest.getTransactionDescription());
      revenue.setRepeatInstance(revenueRequest.getRepeatInstance());
      revenue.setCreditChequeNo(revenueRequest.getCreditChequeNo());
      revenue.setAmount(revenueRequest.getAmount());
      revenue.setBankName(revenueRequest.getBankName());
      return revenueRepository.save(revenue);
    } catch (Exception e) {
      log.error("unable to update revenue error is {} ", e.getMessage());
      return null;
    }
  }

  @Override
  public void deleteRevenueById(String id) {
    revenueRepository.deleteById(id);
  }
}
