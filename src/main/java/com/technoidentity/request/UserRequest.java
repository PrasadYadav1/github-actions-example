package com.technoidentity.request;

import com.technoidentity.enums.Gender;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequest {

  @NotNull(message = "roleId is mandatory")
  private Long roleId;

  @NotBlank(message = "firstName is mandatory")
  private String firstName;

  private String middleName;

  @NotBlank(message = "lastName is mandatory")
  private String lastName;

  private Gender gender;

  @NotBlank(message = "email is mandatory")
  private String email;

  @NotBlank(message = "primaryMobile is mandatory")
  private String primaryMobile;

  private String profileImage;

  private String alternativeMobile;

  private String designation;
}
