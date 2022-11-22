package com.technoidentity.entity;

import java.io.Serializable;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "cash_flow")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CashFlow extends SharedModel implements Serializable {

  @Id
  @GenericGenerator(
      name = "capital_id",
      strategy = "com.technoidentity.generator.CapitalIdGenerator")
  @GeneratedValue(generator = "capital_id")
  @Column(name = "capital_id")
  private String capitalId;

  @Column(name = "date")
  private String date;

  @Column(name = "week")
  private int week;

  @Column(name = "year")
  private int year;

  @Column(name = "capital_started")
  private double capital;

  @Column(name = "inflow")
  private double inFlow;

  @Column(name = "outflow")
  private double outFlow;

  @Column(name = "balance")
  private double balance;

  @Column(name = "funding")
  private double funding;

  @Column(name = "loan")
  private double loan;
}
