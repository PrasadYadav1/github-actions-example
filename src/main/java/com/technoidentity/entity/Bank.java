package com.technoidentity.entity;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bank")
@Data
public class Bank extends SharedModel implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String description;

  private String ifscCode;

  private String accountNumber;
}
