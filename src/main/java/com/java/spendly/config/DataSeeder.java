package com.java.spendly.config;

import com.java.spendly.model.Expense;
import com.java.spendly.repository.ExpenseRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Getter
@Setter
@Data
@Builder
public class DataSeeder implements CommandLineRunner {

    private final ExpenseRepository expenseRepository;

    @Override
    public void run(String... args) throws Exception {
        if (expenseRepository.count() == 0) {
            log.info("Seeding sample expenses...");
            List<Expense> sampleExpenses = List.of(
                    Expense.builder().title("Grocery Shopping").description("Weekly groceries from supermarket")
                            .amount(new BigDecimal("2450.00")).date(LocalDate.now().minusDays(2))
                            .category("Food").paymentMethod(Expense.PaymentMethod.UPI).build(),
                    Expense.builder().title("Electricity Bill").description("Monthly electricity bill")
                            .amount(new BigDecimal("1850.50")).date(LocalDate.now().minusDays(5))
                            .category("Utilities").paymentMethod(Expense.PaymentMethod.NET_BANKING).build(),
                    Expense.builder().title("Netflix Subscription").description("Monthly OTT subscription")
                            .amount(new BigDecimal("649.00")).date(LocalDate.now().minusDays(8))
                            .category("Entertainment").paymentMethod(Expense.PaymentMethod.CREDIT_CARD).build(),
                    Expense.builder().title("Petrol").description("Fuel refill")
                            .amount(new BigDecimal("3000.00")).date(LocalDate.now().minusDays(10))
                            .category("Transport").paymentMethod(Expense.PaymentMethod.DEBIT_CARD).build(),
                    Expense.builder().title("Doctor Visit").description("Routine checkup")
                            .amount(new BigDecimal("500.00")).date(LocalDate.now().minusDays(15))
                            .category("Healthcare").paymentMethod(Expense.PaymentMethod.CASH).build(),
                    Expense.builder().title("Online Course").description("React JS Udemy course")
                            .amount(new BigDecimal("455.00")).date(LocalDate.now().minusDays(20))
                            .category("Education").paymentMethod(Expense.PaymentMethod.CREDIT_CARD).build(),
                    Expense.builder().title("Restaurant Dinner").description("Family dinner at Barbeque Nation")
                            .amount(new BigDecimal("3200.00")).date(LocalDate.now().minusDays(25))
                            .category("Food").paymentMethod(Expense.PaymentMethod.UPI).build(),
                    Expense.builder().title("Gym Membership").description("Monthly gym subscription")
                            .amount(new BigDecimal("1200.00")).date(LocalDate.now().minusMonths(1).minusDays(2))
                            .category("Health & Fitness").paymentMethod(Expense.PaymentMethod.UPI).build(),
                    Expense.builder().title("Mobile Recharge").description("Monthly prepaid recharge")
                            .amount(new BigDecimal("299.00")).date(LocalDate.now().minusMonths(1).minusDays(5))
                            .category("Utilities").paymentMethod(Expense.PaymentMethod.UPI).build(),
                    Expense.builder().title("Amazon Shopping").description("Electronics accessories")
                            .amount(new BigDecimal("1899.00")).date(LocalDate.now().minusMonths(1).minusDays(12))
                            .category("Shopping").paymentMethod(Expense.PaymentMethod.DEBIT_CARD).build()
            );
            expenseRepository.saveAll(sampleExpenses);
            log.info("Sample data seeded successfully.");
        }
    }
}
