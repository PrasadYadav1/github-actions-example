package com.technoidentity.request;

import com.technoidentity.enums.Recurring;
import com.technoidentity.enums.RepeatType;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExpenseRequest {

  @NotNull(message = "transactionDate is mandatory")
  private String transactionDate;

  private String transactionId;

  private String debitChequeNo;

  private String transactionDescription;

  @NotNull(message = "recipient is mandatory")
  private String recipient;

  private String bankName;

  @NotNull(message = "expenseType is mandatory")
  private String expenseType;

  @NotNull(message = "amount is mandatory")
  private Double amount;

  @NotNull(message = "recurringExpense is mandatory")
  private Recurring recurringExpense;

  private RepeatType repeatExpenseType;

  private int repeatInstance;
}
