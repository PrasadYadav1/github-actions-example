package com.technoidentity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CashFlowDto {

  private String capitalId;

  private String date;

  private int week;

  private int year;

  private double capital;

  private double inFlow;

  private double outFlow;

  private double balance;

  private double funding;

  private double loan;
}
