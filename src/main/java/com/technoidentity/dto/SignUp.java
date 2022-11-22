package com.technoidentity.dto;

import com.technoidentity.enums.Gender;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignUp {

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

  @NotBlank(message = "password is mandatory")
  private String password;

  @NotBlank(message = "confirm password is mandatory")
  private String confirmPassword;

  @NotBlank(message = "primaryMobile is mandatory")
  private String primaryMobile;

  private String alternativeMobile;

  private String profileImage;

  private String designation;
}
