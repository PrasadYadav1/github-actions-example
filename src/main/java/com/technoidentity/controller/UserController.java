package com.technoidentity.controller;

import com.technoidentity.dto.UserDto;
import com.technoidentity.entity.User;
import com.technoidentity.exception.ResourceNotFoundException;
import com.technoidentity.request.UserRequest;
import com.technoidentity.service.UserService;
import com.technoidentity.util.CommonResponse;
import com.technoidentity.util.DateFormats;
import com.technoidentity.util.Pagination;
import io.swagger.annotations.Api;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
@Api(tags = "User")
public class UserController {

  @Autowired private UserService userService;
  SimpleDateFormat sm = new DateFormats().DATE_TIME_FORMAT;

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> findById(@PathVariable(value = "id") Long id) {
    UserDto data = userService.getById(id);
    if (data == null) {
      throw new ResourceNotFoundException("User", "id", id);
    }
    return new ResponseEntity<>(data, new HttpHeaders(), HttpStatus.OK);
  }

  @GetMapping(value = "/email")
  public ResponseEntity<List<UserDto>> findByEmail(@RequestParam(required = false) String email) {
    List<UserDto> data = userService.findByEmailContainingIgnoreCase(email);
    return new ResponseEntity<>(data, new HttpHeaders(), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<Pagination> users(
      @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
      @RequestParam(required = false) String search,
      @RequestParam(required = false) Integer status,
      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
      @RequestParam(value = "sortOrder", defaultValue = "DESC") String sortOrder) {
    Sort sortOrderData =
        sortOrder.equals("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable paging = PageRequest.of(pageIndex, pageSize, sortOrderData);
    Pagination data = userService.findByEmailContaining(status, search, paging);

    return new ResponseEntity<>(data, new HttpHeaders(), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateById(
      @PathVariable(value = "id") Long id, @RequestBody UserRequest userRequest) {

    User data = userService.updateById(id, userRequest);

    if (data == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for id " + id);
    }

    return new ResponseEntity(
        new CommonResponse(
            data.getId().toString(),
            sm.format(new Date()),
            HttpServletResponse.SC_OK,
            "",
            "user updated successfully",
            "/api/user"),
        new HttpHeaders(),
        HttpStatus.OK);
  }
}
