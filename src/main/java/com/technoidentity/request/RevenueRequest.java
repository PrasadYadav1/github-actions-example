package com.technoidentity.request;

import com.technoidentity.enums.Recurring;
import com.technoidentity.enums.RepeatType;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RevenueRequest {

  @NotNull(message = "transactionDate is mandatory")
  private String transactionDate;

  private String transactionId;

  private String creditChequeNo;

  private String transactionDescription;

  @NotNull(message = "payer is mandatory")
  private String payer;

  private String bankName;

  @NotNull(message = "revenueType is mandatory")
  private String revenueType;

  @NotNull(message = "amount is mandatory")
  private Double amount;

  @NotNull(message = "recurringRevenue is mandatory")
  private Recurring recurringRevenue;

  private RepeatType repeatRevenueType;

  private int repeatInstance;
}
