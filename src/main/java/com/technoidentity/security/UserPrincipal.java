package com.technoidentity.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.technoidentity.entity.Role;
import com.technoidentity.entity.User;
import com.technoidentity.enums.Gender;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class UserPrincipal implements OAuth2User, UserDetails {
  private Long id;

  private String firstName;

  private String middleName;

  private String lastName;

  private String email;

  private String profileImage;

  @JsonIgnore private String password;
  private String primaryMobile;

  private Gender gender;
  private Role role;
  private Collection<? extends GrantedAuthority> authorities;
  private Map<String, Object> attributes;

  public UserPrincipal(
      Long id,
      String firstName,
      String middleName,
      String lastName,
      String email,
      String password,
      String profileImage,
      String primaryMobile,
      Gender gender,
      Role role,
      Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.profileImage = profileImage;
    this.primaryMobile = primaryMobile;
    this.gender = gender;
    this.role = role;
    this.authorities = authorities;
  }

  public static UserPrincipal create(User user) {
    return new UserPrincipal(
        user.getId(),
        user.getFirstName(),
        user.getMiddleName(),
        user.getLastName(),
        user.getEmail(),
        user.getPassword(),
        user.getProfileImage(),
        user.getPrimaryMobile(),
        user.getGender(),
        user.getRole(),
        Collections.emptyList());
  }

  public static UserPrincipal create(User user, Map<String, Object> attributes) {
    UserPrincipal userPrincipal = UserPrincipal.create(user);
    userPrincipal.setAttributes(attributes);
    return userPrincipal;
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getMiddleName() {
    return middleName;
  }

  public String getPrimaryMobile() {
    return primaryMobile;
  }

  public Gender getGender() {
    return gender;
  }

  public String getProfileImage() {
    return profileImage;
  }

  public Role getRole() {
    return role;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  @Override
  public String getName() {
    return String.valueOf(id);
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }
}
