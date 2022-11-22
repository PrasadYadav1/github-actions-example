package com.technoidentity.generator;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class CapitalIdGenerator implements IdentifierGenerator {

  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object o)
      throws HibernateException {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");

    String prefix = LocalDate.now().format(formatter);

    return prefix + 'C';
  }
}
