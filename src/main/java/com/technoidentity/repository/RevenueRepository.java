package com.technoidentity.repository;

import com.technoidentity.entity.Revenue;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RevenueRepository extends JpaRepository<Revenue, String> {

  @Query("SELECT amount from Revenue where transaction_date = :transactionDate")
  List<Double> findAmountByTransactionDate(@Param("transactionDate") String transactionDate);
}
