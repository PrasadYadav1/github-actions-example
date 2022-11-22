package com.technoidentity.repository;

import com.technoidentity.entity.Expense;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ExpenseRepository extends JpaRepository<Expense, String> {

  @Query("SELECT amount from Expense where transaction_date = :transactionDate")
  List<Double> findAmountByTransactionDate(@Param("transactionDate") String transactionDate);
}
