package com.technoidentity.response;

import com.technoidentity.enums.Recurring;
import com.technoidentity.enums.RepeatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {

  private String expenseId;

  private String transactionDate;

  private String transactionId;

  private String debitChequeNo;

  private String transactionDescription;

  private String bankName;

  private String recipient;

  private String expenseType;

  private Double amount;

  private Recurring recurringExpense;

  private RepeatType repeatExpenseType;

  private int repeatInstance;
}
