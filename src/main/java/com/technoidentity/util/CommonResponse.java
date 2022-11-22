package com.technoidentity.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {
  private String id;
  private String timestamp;
  private Integer status;
  private String error;
  private String message;
  private String path;
}
