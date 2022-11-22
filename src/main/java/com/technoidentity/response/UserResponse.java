package com.technoidentity.response;

import com.technoidentity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
  private Long id;

  private Long roleId;

  private String firstName;

  private String middleName;

  private String lastName;

  private String email;

  private String profileImage;

  private Gender gender;

  private String primaryMobile;

  private String designation;
}
