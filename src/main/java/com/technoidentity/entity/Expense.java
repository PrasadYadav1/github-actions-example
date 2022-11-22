package com.technoidentity.entity;

import com.technoidentity.enums.Recurring;
import com.technoidentity.enums.RepeatType;
import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "expense")
@Data
public class Expense extends SharedModel implements Serializable {

  @Id
  @GenericGenerator(
      name = "expense_id",
      strategy = "com.technoidentity.generator.ExpenseIdGenerator")
  @GeneratedValue(generator = "expense_id")
  @Column(name = "expense_id")
  private String expenseId;

  @Column(name = "transaction_date")
  private String transactionDate;

  @Column(name = "transaction_id")
  private String transactionId;

  @Column(name = "debit_cheque_no")
  private String debitChequeNo;

  @Column(name = "transaction_description")
  private String transactionDescription;

  private String recipient;

  @Column(name = "bank_name")
  private String bankName;

  @Column(name = "expense_type")
  private String expenseType;

  private Double amount;

  @Column(name = "recurring_expense")
  private Recurring recurringExpense;

  @Column(name = "repeat_expense_type")
  private RepeatType repeatExpenseType;

  @Column(name = "repeat_instance")
  private int repeatInstance;
}
