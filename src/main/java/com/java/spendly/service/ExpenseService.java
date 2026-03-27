package com.java.spendly.service;

import com.java.spendly.dto.ExpenseDTO;
import com.java.spendly.model.Expense;
import com.java.spendly.model.User;
import com.java.spendly.repository.ExpenseRepository;
import com.java.spendly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    // ── Resolve logged-in user ────────────────────────────────────────────────
    private User currentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    // ── Create ────────────────────────────────────────────────────────────────
    public ExpenseDTO.Response createExpense(ExpenseDTO.Request request) {
        Expense expense = Expense.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .amount(request.getAmount())
                .date(request.getDate())
                .category(request.getCategory())
                .paymentMethod(request.getPaymentMethod())
                .user(currentUser())
                .build();
        return toResponse(expenseRepository.save(expense));
    }

    // ── Read All (filtered) ───────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<ExpenseDTO.Response> getAllExpenses(String category, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findWithFilters(currentUser(), category, startDate, endDate)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── Read One ──────────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public ExpenseDTO.Response getExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));
        if (!expense.getUser().getId().equals(currentUser().getId())) {
            throw new RuntimeException("Access denied");
        }
        return toResponse(expense);
    }

    // ── Update ────────────────────────────────────────────────────────────────
    public ExpenseDTO.Response updateExpense(Long id, ExpenseDTO.Request request) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));
        if (!expense.getUser().getId().equals(currentUser().getId())) {
            throw new RuntimeException("Access denied");
        }
        expense.setTitle(request.getTitle());
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setCategory(request.getCategory());
        expense.setPaymentMethod(request.getPaymentMethod());
        return toResponse(expenseRepository.save(expense));
    }

    // ── Delete ────────────────────────────────────────────────────────────────
    public void deleteExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));
        if (!expense.getUser().getId().equals(currentUser().getId())) {
            throw new RuntimeException("Access denied");
        }
        expenseRepository.delete(expense);
    }

    // ── Summary ───────────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public ExpenseDTO.Summary getSummary() {
        User user = currentUser();

        BigDecimal totalAmount = Optional.ofNullable(expenseRepository.getTotalAmount(user))
                .orElse(BigDecimal.ZERO);
        long totalCount = expenseRepository.countByUser(user);

        YearMonth currentMonth = YearMonth.now();
        BigDecimal thisMonthAmount = Optional.ofNullable(
                        expenseRepository.getTotalAmountBetween(user, currentMonth.atDay(1), currentMonth.atEndOfMonth()))
                .orElse(BigDecimal.ZERO);

        YearMonth lastMonth = currentMonth.minusMonths(1);
        BigDecimal lastMonthAmount = Optional.ofNullable(
                        expenseRepository.getTotalAmountBetween(user, lastMonth.atDay(1), lastMonth.atEndOfMonth()))
                .orElse(BigDecimal.ZERO);

        Map<String, BigDecimal> byCategory = new LinkedHashMap<>();
        expenseRepository.getAmountByCategory(user)
                .forEach(row -> byCategory.put((String) row[0], (BigDecimal) row[1]));

        Map<String, BigDecimal> byMonth = new LinkedHashMap<>();
        expenseRepository.getAmountByMonth(user).stream().limit(6)
                .forEach(row -> {
                    String key = row[1] + "-" + String.format("%02d", row[0]);
                    byMonth.put(key, (BigDecimal) row[2]);
                });

        return ExpenseDTO.Summary.builder()
                .totalAmount(totalAmount)
                .totalCount(totalCount)
                .thisMonthAmount(thisMonthAmount)
                .lastMonthAmount(lastMonthAmount)
                .byCategory(byCategory)
                .byMonth(byMonth)
                .build();
    }

    // ── Categories ────────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<String> getCategories() {
        return expenseRepository.findAllCategories(currentUser());
    }

    // ── Mapper ────────────────────────────────────────────────────────────────
    private ExpenseDTO.Response toResponse(Expense expense) {
        return ExpenseDTO.Response.builder()
                .id(expense.getId())
                .title(expense.getTitle())
                .description(expense.getDescription())
                .amount(expense.getAmount())
                .date(expense.getDate())
                .category(expense.getCategory())
                .paymentMethod(expense.getPaymentMethod())
                .createdAt(expense.getCreatedAt())
                .updatedAt(expense.getUpdatedAt())
                .build();
    }
}