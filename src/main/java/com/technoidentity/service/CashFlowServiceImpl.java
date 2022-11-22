package com.technoidentity.service;

import com.technoidentity.dto.CashFlowDto;
import com.technoidentity.entity.CashFlow;
import com.technoidentity.entity.Ledger;
import com.technoidentity.exception.CashFlowException;
import com.technoidentity.repository.CashFlowRepository;
import com.technoidentity.repository.LedgerRepository;
import com.technoidentity.request.CashFlowRequest;
import com.technoidentity.request.FirstCashFlowRequest;
import com.technoidentity.util.Pagination;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
public class CashFlowServiceImpl implements CashFlowService {

  @Autowired final Mapper mapper;

  @Autowired private final CashFlowRepository cashFlowRepository;

  @Autowired private final LedgerRepository ledgerRepository;

  @Override
  public void saveOrUpdateCashFlow(
      String transactionDate, Double expenseAmount, Double revenueAmount)
      throws ParseException, CashFlowException {

    CashFlow cashFlow;
    CashFlow result;
    Calendar calendar;

    // will update once security is enabled
    /* UserPrincipal userDetails =
          (UserPrincipal)
    SecurityContextHolder.getContext().getAuthentication().getPrincipal();*/

    cashFlow = cashFlowRepository.findByDate(transactionDate);

    if (cashFlow != null) {
      if (expenseAmount != null) {
        cashFlow.setOutFlow(cashFlow.getOutFlow() + expenseAmount);
        cashFlow.setBalance(cashFlow.getBalance() - expenseAmount);
      }
      if (revenueAmount != null) {
        cashFlow.setInFlow(cashFlow.getInFlow() + revenueAmount);
        cashFlow.setBalance(cashFlow.getBalance() + revenueAmount);
      }

      //   cashFlow.setBalance(cashFlow.getCapital() + cashFlow.getInFlow() -
      // cashFlow.getOutFlow());
      try {
        result = cashFlowRepository.save(cashFlow);
        log.info("updated cash-flow entity successfully for id {} ", result.getCapitalId());
      } catch (Exception e) {
        log.error("could not update cash-flow, error is " + e.getMessage());
        throw new CashFlowException(e.getMessage());
      }
      // save in ledger after discussion
      // saveCashFlowAsLedger(result);
    } else {
      SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
      Date date = format.parse(transactionDate);

      calendar = Calendar.getInstance();
      calendar.setTime(date);

      int day = calendar.get(Calendar.DAY_OF_MONTH);
      String previousDay = String.format("%02d", day - 1);
      String previousDate = transactionDate.replaceFirst(Integer.toString(day), previousDay);

      cashFlow = new CashFlow();
      cashFlow.setDate(transactionDate);
      cashFlow.setCapital(cashFlowRepository.findByDate(previousDate).getBalance());

      if (expenseAmount != null) {
        cashFlow.setOutFlow(expenseAmount);
        cashFlow.setBalance(cashFlow.getCapital() - expenseAmount);
      }
      if (revenueAmount != null) {
        cashFlow.setInFlow(revenueAmount);
        cashFlow.setBalance(cashFlow.getCapital() + revenueAmount);
      }

      cashFlow.setYear(calendar.get(Calendar.YEAR));
      cashFlow.setWeek(calendar.get(Calendar.WEEK_OF_YEAR));
      cashFlow.setCreatedBy(1L);
      cashFlow.setCreatedAt(new Date());
      cashFlow.setUpdatedBy(1L);
      cashFlow.setUpdatedAt(new Date());
      cashFlow.setStatus(1);

      try {
        result = cashFlowRepository.save(cashFlow);
        log.info("created cash-flow entity successfully with id {}", result.getCapitalId());
      } catch (Exception e) {
        log.error("could not create cash-flow, error is " + e.getMessage());
        throw new CashFlowException(e.getMessage());
      }
    }
  }

  @Override
  public CashFlow addFirstCashflow(FirstCashFlowRequest firstCashFlowRequest)
      throws ParseException {

    if (cashFlowRepository.count() == 0) {
      String transactionDate = firstCashFlowRequest.getDate();

      CashFlow cashFlow = new CashFlow();

      cashFlow.setDate(transactionDate);
      cashFlow.setCapital(firstCashFlowRequest.getCapital());
      cashFlow.setBalance(firstCashFlowRequest.getCapital());

      SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
      Date date = format.parse(transactionDate);

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      cashFlow.setYear(calendar.get(Calendar.YEAR));
      cashFlow.setWeek(calendar.get(Calendar.WEEK_OF_YEAR));
      cashFlow.setCreatedAt(new Date());
      cashFlow.setUpdatedAt(new Date());
      cashFlow.setStatus(1);
      return cashFlowRepository.save(cashFlow);
    }
    return null;
  }

  public void saveCashFlowAsLedger(CashFlow cashFlow) {
    Ledger ledger = new Ledger();
    ledger.setCapitalExpenseOrRevenueId(cashFlow.getCapitalId());
    ledger.setDate(cashFlow.getDate());
    ledger.setCapital(cashFlow.getCapital());
    ledger.setBalance(cashFlow.getBalance());
    ledger.setWeek(cashFlow.getWeek());
    ledger.setYear(cashFlow.getYear());
    ledgerRepository.save(ledger);
  }

  @Override
  public CashFlowDto getCashFlowById(String id) {
    try {
      CashFlow cashFlow = cashFlowRepository.getOne(id);
      return mapper.map(cashFlow, CashFlowDto.class);
    } catch (Exception e) {
      log.error("Could not find cash-flow error is {} ", e.getMessage());
      return null;
    }
  }

  @Override
  public Pagination getAllCashFlow(Pageable pageable) {
    Page<CashFlow> cashFlow = cashFlowRepository.findAll(pageable);

    Long total = cashFlow.getTotalElements();
    List<CashFlowDto> cashFlowDtoList =
        cashFlow
            .stream()
            .map(
                c -> {
                  CashFlowDto cashFlowDto = mapper.map(c, CashFlowDto.class);
                  return cashFlowDto;
                })
            .collect(Collectors.toList());

    return new Pagination(cashFlowDtoList, total.intValue());
  }

  @Override
  public CashFlow updateCashFlowById(String id, CashFlowRequest cashFlowRequest) {
    CashFlow cashFlow;
    try {
      cashFlow = cashFlowRepository.getOne(id);
      cashFlow.setFunding(cashFlowRequest.getFunding());
      cashFlow.setLoan(cashFlowRequest.getLoan());
      return cashFlowRepository.save(cashFlow);
    } catch (Exception e) {
      log.error("unable to update cash-flow error is {} ", e.getMessage());
      return null;
    }
  }

  @Override
  public void deleteCashFlowById(String id) {
    cashFlowRepository.deleteById(id);
  }
}
