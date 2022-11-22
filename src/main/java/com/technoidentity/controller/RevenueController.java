package com.technoidentity.controller;

import com.technoidentity.entity.Revenue;
import com.technoidentity.request.RevenueRequest;
import com.technoidentity.service.RevenueService;
import com.technoidentity.util.CommonResponse;
import com.technoidentity.util.DateFormats;
import com.technoidentity.util.Pagination;
import com.technoidentity.util.ResponseMessage;
import io.swagger.annotations.Api;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/revenue")
@CrossOrigin
@Api(tags = "Revenue")
public class RevenueController {

  @Autowired private RevenueService revenueService;
  SimpleDateFormat sm = new DateFormats().DATE_TIME_FORMAT;

  @GetMapping("/all")
  public ResponseEntity<Pagination> showAllRevenues(
      @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
      @RequestParam(value = "sortBy", defaultValue = "revenueId") String sortBy,
      @RequestParam(value = "sortOrder", defaultValue = "DESC") String sortOrder) {
    Sort sortOrderData =
        sortOrder.equals("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable paging = PageRequest.of(pageIndex, pageSize, sortOrderData);
    Pagination data = revenueService.getAllRevenues(paging);

    return new ResponseEntity<>(data, new HttpHeaders(), HttpStatus.OK);
  }

  @PostMapping("/create")
  public ResponseEntity<?> addRevenue(@Valid @RequestBody RevenueRequest revenueRequest) {
    Revenue revenue;
    String message = "";
    try {
      // will update this code once security is enabled for this controller
      //  UserPrincipal userDetails =
      //        (UserPrincipal)
      // SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      revenue = revenueService.addRevenue(revenueRequest, 1L);
    } catch (Exception e) {
      message = "Could not create revenue " + e.getMessage() + "!";
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
          .body(new ResponseMessage(message));
    }
    return new ResponseEntity(
        new CommonResponse(
            revenue.getRevenueId(),
            sm.format(new Date()),
            HttpServletResponse.SC_OK,
            "",
            "revenue created successfully",
            "/revenue/create"),
        new HttpHeaders(),
        HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateRevenue(
      @PathVariable("id") String id, @Valid @RequestBody RevenueRequest revenue) {
    Revenue data = revenueService.updateRevenue(id, revenue);

    if (data == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("Revenue not found for given id " + id);
    }

    return new ResponseEntity(
        new CommonResponse(
            data.getRevenueId(),
            sm.format(new Date()),
            HttpServletResponse.SC_OK,
            "",
            "Revenue updated successfully",
            "/revenue"),
        new HttpHeaders(),
        HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteRevenue(@PathVariable("id") String id) {
    try {
      revenueService.deleteRevenueById(id);
      return new ResponseEntity<>(
          "Revenue deleted successfully for id: " + id, new HttpHeaders(), HttpStatus.OK);
    } catch (Exception e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }
}
