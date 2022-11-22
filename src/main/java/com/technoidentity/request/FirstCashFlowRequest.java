package com.technoidentity.request;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FirstCashFlowRequest {

  @NotNull(message = "date is mandatory")
  private String date;

  private double capital;
}
