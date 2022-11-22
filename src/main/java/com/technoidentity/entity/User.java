package com.technoidentity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.technoidentity.enums.AuthProvider;
import com.technoidentity.enums.Gender;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends SharedModel implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "role_id")
  private Long roleId;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "role_id", nullable = false, insertable = false, updatable = false)
  private Role role;

  @Column(nullable = false, name = "first_name")
  private String firstName;

  @Column(name = "middle_name")
  private String middleName;

  @Column(nullable = false, name = "last_name")
  private String lastName;

  @Email
  @Column(nullable = false)
  private String email;

  @Column(name = "profile_image")
  private String profileImage;

  @Column(nullable = false, name = "email_verified")
  private Boolean emailVerified = false;

  @JsonIgnore private String password;

  @NotNull
  @Enumerated(EnumType.STRING)
  private AuthProvider provider;

  @Column(name = "provider_id")
  private String providerId;

  private Gender gender;

  @Column(name = "primary_mobile")
  private String primaryMobile;

  @Column(name = "alternative_mobile")
  private String alternativeMobile;

  @Column(name = "designation")
  private String designation;
}
