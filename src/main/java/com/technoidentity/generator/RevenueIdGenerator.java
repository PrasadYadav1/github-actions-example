package com.technoidentity.generator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class RevenueIdGenerator implements IdentifierGenerator {

  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object o)
      throws HibernateException {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");

    String date = LocalDate.now().format(formatter);
    String prefix = "'" + date + '%' + "'";

    Connection connection = session.connection();

    try {
      Statement statement = connection.createStatement();

      ResultSet rs =
          statement.executeQuery(
              "select count(revenue_id) as Id from Revenue where revenue_id like " + prefix);

      if (rs.next()) {
        int id = rs.getInt(1) + 1;
        return date + 'R' + id;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }
}
