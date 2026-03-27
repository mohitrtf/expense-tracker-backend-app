package com.java.spendly.repository;

import com.java.spendly.model.Expense;
import com.java.spendly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Filtered list for a specific user
    @Query("SELECT e FROM Expense e WHERE e.user = :user " +
            "AND (:category IS NULL OR e.category = :category) " +
            "AND (:startDate IS NULL OR e.date >= :startDate) " +
            "AND (:endDate IS NULL OR e.date <= :endDate) " +
            "ORDER BY e.date DESC")
    List<Expense> findWithFilters(
            @Param("user") User user,
            @Param("category") String category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user")
    BigDecimal getTotalAmount(@Param("user") User user);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user " +
            "AND e.date BETWEEN :startDate AND :endDate")
    BigDecimal getTotalAmountBetween(@Param("user") User user,
                                     @Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);

    @Query("SELECT e.category, SUM(e.amount) FROM Expense e WHERE e.user = :user GROUP BY e.category")
    List<Object[]> getAmountByCategory(@Param("user") User user);

    @Query("SELECT FUNCTION('MONTH', e.date), FUNCTION('YEAR', e.date), SUM(e.amount) " +
            "FROM Expense e WHERE e.user = :user " +
            "GROUP BY FUNCTION('YEAR', e.date), FUNCTION('MONTH', e.date) " +
            "ORDER BY FUNCTION('YEAR', e.date) DESC, FUNCTION('MONTH', e.date) DESC")
    List<Object[]> getAmountByMonth(@Param("user") User user);

    @Query("SELECT DISTINCT e.category FROM Expense e WHERE e.user = :user ORDER BY e.category")
    List<String> findAllCategories(@Param("user") User user);

    long countByUser(User user);
}