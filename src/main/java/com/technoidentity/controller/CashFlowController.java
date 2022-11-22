package com.technoidentity.controller;

import com.technoidentity.dto.CashFlowDto;
import com.technoidentity.entity.CashFlow;
import com.technoidentity.request.CashFlowRequest;
import com.technoidentity.request.FirstCashFlowRequest;
import com.technoidentity.service.CashFlowService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/cash-flow")
@CrossOrigin
@Api(tags = "CashFlow")
public class CashFlowController {

  @Autowired private CashFlowService cashFlowService;
  SimpleDateFormat sm = new DateFormats().DATE_TIME_FORMAT;

  @PostMapping("/first-create")
  public ResponseEntity<?> addFirstCashFlow(
      @Valid @RequestBody FirstCashFlowRequest cashFlowRequest) {
    CashFlow data;
    String message;
    try {
      data = cashFlowService.addFirstCashflow(cashFlowRequest);
      if (data != null) {
        return new ResponseEntity(
            new CommonResponse(
                data.getCapitalId(),
                sm.format(new Date()),
                HttpServletResponse.SC_OK,
                "",
                "first cash-flow created successfully",
                "/api/first-cash-flow/create"),
            new HttpHeaders(),
            HttpStatus.OK);
      } else {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
            .body("cash-flow already exists");
      }
    } catch (Exception e) {
      message = "Could not create cash-flow " + e.getMessage() + "!";
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
          .body(new ResponseMessage(message));
    }
  }

  @GetMapping("/all")
  public ResponseEntity<Pagination> showAllCashFlows(
      @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
      @RequestParam(value = "sortBy", defaultValue = "capitalId") String sortBy,
      @RequestParam(value = "sortOrder", defaultValue = "DESC") String sortOrder) {
    Sort sortOrderData =
        sortOrder.equals("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable paging = PageRequest.of(pageIndex, pageSize, sortOrderData);
    Pagination data = cashFlowService.getAllCashFlow(paging);

    return new ResponseEntity<>(data, new HttpHeaders(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable("id") String id) {

    CashFlowDto data = cashFlowService.getCashFlowById(id);
    if (data == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("cash-flow not found for id " + id);
    }

    return new ResponseEntity<>(data, new HttpHeaders(), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateCashFlow(
      @PathVariable("id") String id, @Valid @RequestBody CashFlowRequest cashFlowRequest) {
    CashFlow data = cashFlowService.updateCashFlowById(id, cashFlowRequest);

    if (data == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("Cash-flow not found for given id " + id);
    }

    return new ResponseEntity(
        new CommonResponse(
            data.getCapitalId(),
            sm.format(new Date()),
            HttpServletResponse.SC_OK,
            "",
            "cash-flow updated successfully",
            "/api/cash-flow"),
        new HttpHeaders(),
        HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteCashFlow(@PathVariable("id") String id) {
    try {
      cashFlowService.deleteCashFlowById(id);
      return new ResponseEntity<>(
          "Cash-flow deleted successfully for id: " + id, new HttpHeaders(), HttpStatus.OK);
    } catch (Exception e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }
}
