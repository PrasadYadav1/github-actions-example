package com.technoidentity.service;

import com.technoidentity.dto.SignUp;
import com.technoidentity.dto.UserDto;
import com.technoidentity.entity.User;
import com.technoidentity.enums.AuthProvider;
import com.technoidentity.exception.ResourceNotFoundException;
import com.technoidentity.repository.UserRepository;
import com.technoidentity.request.UserRequest;
import com.technoidentity.security.UserPrincipal;
import com.technoidentity.util.Pagination;
import com.technoidentity.util.StringPredicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  @Autowired final Mapper mapper;

  @Autowired private final UserRepository userRepository;

  @Autowired private final PasswordEncoder passwordEncoder;

  StringPredicate isNullOrEmpty = (str) -> str == null || str.trim().isEmpty();

  @Override
  public User add(SignUp signUp) {
    User user = new User();
    user.setEmail(signUp.getEmail());
    user.setPassword(signUp.getPassword());
    user.setProvider(AuthProvider.local);
    user.setFirstName(signUp.getFirstName());
    user.setMiddleName(signUp.getMiddleName());
    user.setLastName(signUp.getLastName());
    user.setGender(signUp.getGender());
    user.setPrimaryMobile(signUp.getPrimaryMobile());
    user.setAlternativeMobile(signUp.getAlternativeMobile());
    user.setDesignation(signUp.getDesignation());
    user.setCreatedBy(1L);
    user.setCreatedAt(new Date());
    user.setUpdatedBy(1L);
    user.setUpdatedAt(new Date());
    user.setStatus(1);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRoleId(signUp.getRoleId());
    return userRepository.save(user);
  }

  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found with email : " + email));

    return UserPrincipal.create(user);
  }

  public UserDetails loadUserById(String id) {
    User user =
        userRepository
            .findById(Long.parseLong(id))
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    return UserPrincipal.create(user);
  }

  @Override
  public Boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public UserDto getById(Long id) {
    try {
      User user = userRepository.getOne(id);
      return mapper.map(user, UserDto.class);

    } catch (Exception e) {
      log.error("Could not find user error is {} ", e.getMessage());
      return null;
    }
  }

  @Override
  public List<UserDto> findByEmailContainingIgnoreCase(String email) {
    List<UserDto> userDtoList = new ArrayList<>();
    if (isNullOrEmpty.apply(email)) {
      List<User> users = userRepository.findAll();
      userDtoList =
          users
              .stream()
              .map(
                  x -> {
                    UserDto cl = mapper.map(x, UserDto.class);
                    cl.setRoleName(x.getRole().getName());
                    return cl;
                  })
              .collect(Collectors.toList());
      return userDtoList;
    }
    List<User> users = userRepository.findByEmailContainingIgnoreCase(email);
    userDtoList =
        users
            .stream()
            .map(
                x -> {
                  UserDto cl = mapper.map(x, UserDto.class);
                  cl.setRoleName(x.getRole().getName());
                  return cl;
                })
            .collect(Collectors.toList());
    return userDtoList;
  }

  @Override
  public Pagination findByEmailContaining(Integer status, String email, Pageable pageable) {
    Page<User> user = searchUser(email, status, pageable);
    Long total = user.getTotalElements();
    List<UserDto> userDtoList =
        user.stream()
            .map(
                c -> {
                  UserDto result = mapper.map(c, UserDto.class);
                  result.setRoleName(c.getRole().getName());
                  return result;
                })
            .collect(Collectors.toList());

    return new Pagination(userDtoList, total.intValue());
  }

  @Override
  public User updateById(Long id, UserRequest userRequest) {
    User user;
    try {
      user = userRepository.getOne(id);
      user.setFirstName(userRequest.getFirstName());
      user.setMiddleName(userRequest.getMiddleName());
      user.setLastName(userRequest.getLastName());
      user.setDesignation(userRequest.getDesignation());
      user.setGender(userRequest.getGender());
      user.setPrimaryMobile(userRequest.getPrimaryMobile());
      user.setRoleId(userRequest.getRoleId());
      user.setAlternativeMobile(userRequest.getAlternativeMobile());
      user.setProfileImage(userRequest.getProfileImage());
      return userRepository.save(user);
    } catch (Exception e) {
      log.error("unable to update user error is {} ", e.getMessage());
      return null;
    }
  }

  private Page<User> searchUser(String search, Integer status, Pageable pageable) {
    if (isNullOrEmpty.apply(search) && status == null) {
      return userRepository.findAll(pageable);
    }
    if (isNullOrEmpty.apply(search) && status != null) {
      return userRepository.findByStatus(status, pageable);
    }
    if (!isNullOrEmpty.apply(search) && status != null) {
      return userRepository.findByStatusAndEmailContainingIgnoreCase(status, search, pageable);
    }
    return userRepository.findByStatusAndEmailContainingIgnoreCase(status, search, pageable);
  }
}
