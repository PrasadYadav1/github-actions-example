package com.technoidentity.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.technoidentity.dto.SignUp;
import com.technoidentity.dto.UserDto;
import com.technoidentity.entity.User;
import com.technoidentity.enums.AuthProvider;
import com.technoidentity.enums.Gender;
import com.technoidentity.exception.ResourceNotFoundException;
import com.technoidentity.repository.UserRepository;
import com.technoidentity.request.UserRequest;
import java.util.Date;
import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  private UserService service;

  @Mock private UserRepository userRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private Mapper mapper;

  @Before
  public void setup() {
    service = new UserServiceImpl(mapper, userRepository, passwordEncoder);
  }

  @Test
  public void createUserTest() {
    User user = getUser();
    user.setId(1L);

    // when
    when(userRepository.save(any(User.class))).thenReturn(user);

    // method call
    User result = service.add(getSignUpData());

    // verify
    verify(userRepository, times(1)).save(any(User.class));
    assertNotNull(result);
    assertEquals("1", result.getId().toString());
    assertEquals("Ravi", result.getFirstName());
    assertEquals("12345", result.getPrimaryMobile());
    assertEquals("Developer", result.getDesignation());
  }

  @Test
  public void getUserTest() {
    User user = getUser();
    user.setId(1L);

    // when
    when(userRepository.getOne(anyLong())).thenReturn(user);

    // method call
    service.getById(1L);

    // verify
    verify(userRepository, times(1)).getOne(any());
  }

  @Test
  public void getUserShouldReturnNullWhenIdNotFound() {
    User user = getUser();
    user.setId(1L);

    // when
    when(userRepository.getOne(anyLong()))
        .thenThrow(new ResourceNotFoundException("User", "id", user.getId()));

    // method call
    UserDto result = service.getById(1L);

    // verify
    verify(userRepository, times(1)).getOne(any());
    assertNull(result);
  }

  @Test
  public void updateUserTest() {
    User user = getUser();

    UserRequest userRequest = getUserRequest();

    // when
    when(userRepository.getOne(anyLong())).thenReturn(user);
    when(userRepository.save(any(User.class))).thenReturn(user);

    // method call
    User result = service.updateById(2L, userRequest);

    // verify
    verify(userRepository, times(1)).getOne(any());

    verify(userRepository, times(1)).save(any(User.class));
    assertNotNull(result);
  }

  @Test
  public void updateUserShouldReturnNullWhenIdNotFoundTest() {
    User user = getUser();

    UserRequest userRequest = getUserRequest();

    // when
    when(userRepository.getOne(anyLong()))
        .thenThrow(new ResourceNotFoundException("User", "id", user.getId()));

    // method call
    User result = service.updateById(2L, userRequest);

    // verify
    verify(userRepository, times(1)).getOne(any());

    verify(userRepository, times(0)).save(any(User.class));
    assertNull(result);
  }

  public User getUser() {
    SignUp signUp = getSignUpData();
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
    user.setRoleId(signUp.getRoleId());
    return user;
  }

  public UserRequest getUserRequest() {
    UserRequest user = new UserRequest();
    user.setEmail("ravi@gmail.com");
    user.setFirstName("Raghav");
    user.setMiddleName("Chandra");
    user.setLastName("Shekar");
    user.setGender(Gender.Male);
    user.setPrimaryMobile("12345");
    user.setAlternativeMobile("4445");
    user.setDesignation("Developer");
    user.setRoleId(2L);
    return user;
  }

  public SignUp getSignUpData() {
    SignUp user = new SignUp();
    user.setFirstName("Ravi");
    user.setMiddleName("Chandra");
    user.setLastName("Shekar");
    user.setGender(Gender.Male);
    user.setRoleId(1L);
    user.setDesignation("Developer");
    user.setEmail("ravi@gmail.com");
    user.setPrimaryMobile("12345");
    user.setPassword("abcd123");
    return user;
  }
}
