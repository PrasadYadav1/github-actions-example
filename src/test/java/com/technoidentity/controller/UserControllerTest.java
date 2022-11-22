package com.technoidentity.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoidentity.dto.UserDto;
import com.technoidentity.entity.User;
import com.technoidentity.enums.Gender;
import com.technoidentity.exception.ResourceNotFoundException;
import com.technoidentity.request.UserRequest;
import com.technoidentity.security.CustomUserDetailsService;
import com.technoidentity.security.TokenAuthenticationFilter;
import com.technoidentity.security.oauth2.CustomOAuth2UserService;
import com.technoidentity.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.technoidentity.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.technoidentity.service.UserService;
import com.technoidentity.util.Pagination;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

  private static final String USER_API = "/api/user";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private UserService userService;

  @MockBean private CustomUserDetailsService customUserDetailsService;

  @MockBean private CustomOAuth2UserService customOAuth2UserService;

  @MockBean private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

  @MockBean private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

  @MockBean private TokenAuthenticationFilter tokenAuthenticationFilter;

  @Test
  public void getAllUsersTest() throws Exception {

    UserDto userDto = getUserDto();
    Pagination page = new Pagination(Collections.singletonList(userDto), 1);

    // given
    when(userService.findByEmailContaining(anyInt(), anyString(), any(Pageable.class)))
        .thenReturn(page);

    // when & then
    ResultActions result =
        this.mockMvc
            .perform(get(USER_API).param("search", "abc@gmail.com").param("status", "1"))
            .andDo(print())
            .andExpect(status().isOk());

    result.andExpect(jsonPath("$.data.length()", is(1)));
    result.andExpect(jsonPath("$.data[0].firstName", is("AAA")));
    result.andExpect(jsonPath("$.data[0].lastName", is("BBB")));
    result.andExpect(jsonPath("$.data[0].email", is("abc@gmail.com")));
    result.andExpect(jsonPath("$.data[0].gender", is("Male")));
    result.andExpect(jsonPath("$.data[0].designation", is("Developer")));
  }

  @Test
  public void updateUserTest() throws Exception {

    UserDto userDto = getUserDto();

    // given
    when(userService.getById(anyLong())).thenReturn(userDto);

    // when & then
    ResultActions result =
        this.mockMvc.perform(get(USER_API + "/2")).andDo(print()).andExpect(status().isOk());

    result.andExpect(jsonPath("$.firstName", is("AAA")));
    result.andExpect(jsonPath("$.lastName", is("BBB")));
    result.andExpect(jsonPath("$.email", is("abc@gmail.com")));
    result.andExpect(jsonPath("$.gender", is("Male")));
    result.andExpect(jsonPath("$.designation", is("Developer")));
  }

  @Test
  public void getUserShouldReturnExceptionWhenIdNotFound() throws Exception {

    var errorMessage = "User not found with id : '2'";
    // given
    when(userService.getById(anyLong())).thenReturn(null);

    // when & then
    this.mockMvc
        .perform(get(USER_API + "/2"))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(
            result ->
                Assert.assertTrue(
                    result.getResolvedException() instanceof ResourceNotFoundException))
        .andExpect(
            result ->
                Assert.assertEquals(
                    errorMessage,
                    Objects.requireNonNull(result.getResolvedException()).getMessage()));
  }

  @Test
  public void getUserShouldReturnRecordWhenIdIsFound() throws Exception {

    UserDto user = getUserDto();

    // given
    when(userService.getById(anyLong())).thenReturn(user);

    // when & then
    this.mockMvc.perform(get(USER_API + "/1")).andDo(print()).andExpect(status().isOk());
  }

  @Test
  public void updateUserWhenIdFoundTest() throws Exception {
    UserRequest request = new UserRequest();
    request.setEmail("xyz@gmail.com");

    User user = new User();
    user.setId(3L);
    user.setFirstName("Arnav");
    user.setLastName("kumar");
    user.setRoleId(2L);
    user.setPrimaryMobile("12345");
    user.setEmail("xyz@gmail.com");
    user.setProfileImage("abc.jpeg");

    // given
    when(userService.updateById(any(), any(UserRequest.class))).thenReturn(user);

    // when & then
    ResultActions result =
        this.mockMvc
            .perform(
                put(USER_API + "/3")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

    result.andExpect(jsonPath("$.id", is("3")));
    result.andExpect(jsonPath("$.message", is("user updated successfully")));
    result.andExpect(jsonPath("$.path", is("/api/user")));
  }

  @Test
  public void updateUserShouldReturnExceptionWhenIdNotFound() throws Exception {
    UserRequest request = new UserRequest();
    request.setEmail("xyz@gmail.com");
    request.setFirstName("Arnav");
    request.setLastName("kumar");
    request.setRoleId(2L);
    request.setPrimaryMobile("12345");

    var errorMessage = "User not found for id 3";
    // given
    when(userService.updateById(any(), any(UserRequest.class))).thenReturn(null);

    // when & then
    this.mockMvc
        .perform(
            put(USER_API + "/3")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$", is(errorMessage)));
  }

  @Test
  public void findByEmailTest() throws Exception {

    UserDto userDto = getUserDto();

    // given
    when(userService.findByEmailContainingIgnoreCase(anyString()))
        .thenReturn(Arrays.asList(userDto));

    // when & then
    ResultActions result =
        this.mockMvc
            .perform(get(USER_API + "/email").param("email", "abc@gmail.com"))
            .andDo(print())
            .andExpect(status().isOk());

    result.andExpect(jsonPath("$.length()", is(1)));
    result.andExpect(jsonPath("$[0].firstName", is("AAA")));
    result.andExpect(jsonPath("$.[0].lastName", is("BBB")));
    result.andExpect(jsonPath("$.[0].email", is("abc@gmail.com")));
    result.andExpect(jsonPath("$.[0].gender", is("Male")));
    result.andExpect(jsonPath("$.[0].designation", is("Developer")));
  }

  public UserDto getUserDto() {
    UserDto userDto = new UserDto();
    userDto.setFirstName("AAA");
    userDto.setLastName("BBB");
    userDto.setEmail("abc@gmail.com");
    userDto.setDesignation("Developer");
    userDto.setGender(Gender.Male);
    return userDto;
  }
}
