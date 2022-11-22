package com.technoidentity.dto;

import com.technoidentity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
  private Long id;

  private Long roleId;

  private String roleName;

  private String firstName;

  private String middleName;

  private String lastName;

  private String email;

  private String profileImage;

  private Gender gender;

  private String primaryMobile;

  private String designation;
}
