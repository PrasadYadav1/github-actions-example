package com.technoidentity.controller;

import com.technoidentity.entity.Expense;
import com.technoidentity.request.ExpenseRequest;
import com.technoidentity.service.ExpenseService;
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
@RequestMapping(value = "/expense")
@CrossOrigin
@Api(tags = "Expense")
public class ExpenseController {

  @Autowired private ExpenseService expenseService;
  SimpleDateFormat sm = new DateFormats().DATE_TIME_FORMAT;

  @GetMapping("/all")
  public ResponseEntity<Pagination> showAllExpenses(
      @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
      @RequestParam(value = "sortBy", defaultValue = "expenseId") String sortBy,
      @RequestParam(value = "sortOrder", defaultValue = "DESC") String sortOrder) {
    Sort sortOrderData =
        sortOrder.equals("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable paging = PageRequest.of(pageIndex, pageSize, sortOrderData);
    Pagination data = expenseService.getAllExpenses(paging);

    return new ResponseEntity<>(data, new HttpHeaders(), HttpStatus.OK);
  }

  @PostMapping("/create")
  public ResponseEntity<?> addExpense(@Valid @RequestBody ExpenseRequest expenseRequest) {
    Expense expense;
    String message = "";
    try {
      // will update this code once security is enabled for this controller
      //  UserPrincipal userDetails =
      //        (UserPrincipal)
      // SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      expense = expenseService.addExpense(expenseRequest, 1L);
    } catch (Exception e) {
      message = "Could not create expense " + e.getMessage() + "!";
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
          .body(new ResponseMessage(message));
    }
    return new ResponseEntity(
        new CommonResponse(
            expense.getExpenseId(),
            sm.format(new Date()),
            HttpServletResponse.SC_OK,
            "",
            "expense created successfully",
            "/expense/create"),
        new HttpHeaders(),
        HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateExpense(
      @PathVariable("id") String id, @Valid @RequestBody ExpenseRequest expense) {
    Expense data = expenseService.updateExpense(id, expense);

    if (data == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("expense not found for given id " + id);
    }

    return new ResponseEntity(
        new CommonResponse(
            data.getExpenseId(),
            sm.format(new Date()),
            HttpServletResponse.SC_OK,
            "",
            "expense updated successfully",
            "/expense"),
        new HttpHeaders(),
        HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteExpense(@PathVariable("id") String id) {
    try {
      expenseService.deleteExpenseById(id);
      return new ResponseEntity<>(
          "Expense deleted successfully for id: " + id, new HttpHeaders(), HttpStatus.OK);
    } catch (Exception e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }
}
