package com.technoidentity.service;

import com.technoidentity.entity.Revenue;
import com.technoidentity.exception.CashFlowException;
import com.technoidentity.request.RevenueRequest;
import com.technoidentity.util.Pagination;
import org.springframework.data.domain.Pageable;

public interface RevenueService {

  Revenue addRevenue(RevenueRequest revenueRequest, Long userId) throws CashFlowException;

  Pagination getAllRevenues(Pageable pageable);

  Revenue updateRevenue(String id, RevenueRequest revenueRequest);

  void deleteRevenueById(String id);
}
