package com.technoidentity.entity;

import com.technoidentity.enums.Recurring;
import com.technoidentity.enums.RepeatType;
import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "revenue")
@Data
public class Revenue extends SharedModel implements Serializable {

  @Id
  @GenericGenerator(
      name = "revenue_id",
      strategy = "com.technoidentity.generator.RevenueIdGenerator")
  @GeneratedValue(generator = "revenue_id")
  @Column(name = "revenue_id")
  private String revenueId;

  @Column(name = "transaction_date")
  private String transactionDate;

  @Column(name = "transaction_id")
  private String transactionId;

  @Column(name = "credit_cheque_no")
  private String creditChequeNo;

  @Column(name = "transaction_description")
  private String transactionDescription;

  private String payer;

  @Column(name = "bank_name")
  private String bankName;

  @Column(name = "revenue_type")
  private String revenueType;

  private Double amount;

  @Column(name = "recurring_revenue")
  private Recurring recurringRevenue;

  @Column(name = "repeat_revenue_type")
  private RepeatType repeatRevenueType;

  @Column(name = "repeat_instance")
  private int repeatInstance;
}
