package com.technoidentity.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
public class SharedModel {

  @Basic
  @Column(name = "created_by")
  private Long createdBy;

  @Basic
  @Column(name = "updated_by")
  private Long updatedBy;

  @Basic
  @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  public Date createdAt;

  @Basic
  @Column(name = "updated_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private Date updatedAt;

  @Basic
  @Column(name = "status")
  private Integer status;
}
