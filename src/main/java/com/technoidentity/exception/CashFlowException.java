package com.technoidentity.exception;

public class CashFlowException extends Exception {

  public CashFlowException() {}

  public CashFlowException(String message) {
    super(message);
  }

  public CashFlowException(String message, Throwable cause) {
    super(message, cause);
  }
}
