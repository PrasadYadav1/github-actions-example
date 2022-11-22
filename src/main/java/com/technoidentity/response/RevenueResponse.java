package com.technoidentity.response;

import com.technoidentity.enums.Recurring;
import com.technoidentity.enums.RepeatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueResponse {

  private String revenueId;

  private String transactionDate;

  private String transactionId;

  private String creditChequeNo;

  private String transactionDescription;

  private String bankName;

  private String payer;

  private String revenueType;

  private Double amount;

  private Recurring recurringRevenue;

  private RepeatType repeatRevenueType;

  private int repeatInstance;
}
