package com.java.spendly.controller;

import com.java.spendly.dto.ExpenseDTO;
import com.java.spendly.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@CrossOrigin(origins = "${app.cors.allowed-origins:http://localhost:3000}")
public class ExpenseController {

    private final ExpenseService expenseService;

    // POST /api/expenses
    @PostMapping
    public ResponseEntity<ExpenseDTO.Response> createExpense(
            @Valid @RequestBody ExpenseDTO.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(expenseService.createExpense(request));
    }

    // GET /api/expenses
    @GetMapping
    public ResponseEntity<List<ExpenseDTO.Response>> getAllExpenses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(expenseService.getAllExpenses(category, startDate, endDate));
    }

    // GET /api/expenses/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO.Response> getExpenseById(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.getExpenseById(id));
    }

    // PUT /api/expenses/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDTO.Response> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseDTO.Request request) {
        return ResponseEntity.ok(expenseService.updateExpense(id, request));
    }

    // DELETE /api/expenses/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/expenses/summary
    @GetMapping("/summary")
    public ResponseEntity<ExpenseDTO.Summary> getSummary() {
        return ResponseEntity.ok(expenseService.getSummary());
    }

    // GET /api/expenses/categories
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(expenseService.getCategories());
    }
}