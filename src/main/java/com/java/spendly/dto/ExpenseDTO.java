package com.java.spendly.dto;

import com.java.spendly.model.Expense.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpenseDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Title is required")
        @Size(min = 1, max = 100)
        private String title;

        @Size(max = 500)
        private String description;

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01")
        private BigDecimal amount;

        @NotNull(message = "Date is required")
        private LocalDate date;

        @NotBlank(message = "Category is required")
        private String category;

        private PaymentMethod paymentMethod = PaymentMethod.CASH;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String title;
        private String description;
        private BigDecimal amount;
        private LocalDate date;
        private String category;
        private PaymentMethod paymentMethod;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Summary {
        private BigDecimal totalAmount;
        private long totalCount;
        private BigDecimal thisMonthAmount;
        private BigDecimal lastMonthAmount;
        private java.util.Map<String, BigDecimal> byCategory;
        private java.util.Map<String, BigDecimal> byMonth;
    }
}
