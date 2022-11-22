package com.technoidentity.controller;

import com.technoidentity.dto.*;
import com.technoidentity.entity.User;
import com.technoidentity.exception.BadRequestException;
import com.technoidentity.security.TokenProvider;
import com.technoidentity.security.UserPrincipal;
import com.technoidentity.service.UserService;
import com.technoidentity.util.CommonResponse;
import com.technoidentity.util.DateFormats;
import io.swagger.annotations.Api;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@Api(tags = "Authentication")
public class AuthController {

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private UserService userService;

  @Autowired private TokenProvider tokenProvider;

  SimpleDateFormat sm = new DateFormats().DATE_TIME_FORMAT;

  @PostMapping("/sign-in")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignIn signIn) {

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(signIn.getEmail(), signIn.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String token = tokenProvider.createToken(authentication);
    UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
    UserDto userDto =
        new UserDto(
            userDetails.getId(),
            userDetails.getRole().getId(),
            userDetails.getRole().getName(),
            userDetails.getFirstName(),
            userDetails.getMiddleName(),
            userDetails.getLastName(),
            userDetails.getEmail(),
            userDetails.getProfileImage(),
            userDetails.getGender(),
            userDetails.getPrimaryMobile(),
            "");
    return ResponseEntity.ok(new SignInResponseDto(token, "Bearer", userDto));
  }

  @PostMapping("/sign-up")
  public ResponseEntity<?> addUser(@Valid @RequestBody SignUp signUp) {
    if (userService.existsByEmail(signUp.getEmail())) {
      throw new BadRequestException("Email address already in use.");
    }
    User user = userService.add(signUp);
    return new ResponseEntity(
        new CommonResponse(
            user.getId().toString(),
            sm.format(new Date()),
            HttpServletResponse.SC_OK,
            "",
            "user added successfully",
            "/api/auth"),
        new HttpHeaders(),
        HttpStatus.OK);
  }
}
