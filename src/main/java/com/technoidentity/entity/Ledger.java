package com.technoidentity.entity;

import com.technoidentity.enums.Recurring;
import com.technoidentity.enums.RepeatType;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ledger")
@Data
public class Ledger {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "date")
  private String date;

  @Column(name = "cap_exp_rev_id")
  private String capitalExpenseOrRevenueId;

  @Column(name = "week")
  private int week;

  @Column(name = "year")
  private int year;

  @Column(name = "transaction_id")
  private String transactionId;

  @Column(name = "cheque_no")
  private String chequeNo;

  @Column(name = "transaction_description")
  private String transactionDescription;

  @Column(name = "recipient_payee")
  private String recipientOrPayee;

  @Column(name = "bank_name")
  private String bankName;

  @Column(name = "expense_revenue_type")
  private String expenseOrRevenueType;

  private Double capital;

  private Double amount;

  private Double balance;

  @Column(name = "recurring")
  private Recurring recurring;

  @Column(name = "repeat_type")
  private RepeatType repeatType;

  @Column(name = "repeat_instance")
  private int repeatInstance;
}
