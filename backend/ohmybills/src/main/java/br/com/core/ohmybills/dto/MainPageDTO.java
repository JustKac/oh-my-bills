package br.com.core.ohmybills.dto;

import java.math.BigDecimal;
import java.util.Map;

public record MainPageDTO(
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal totalRecurrence,
        Map<String, BigDecimal> totalExpenseByCreditCard,
        Map<String, BigDecimal> totalExpenseByTag
) {}
