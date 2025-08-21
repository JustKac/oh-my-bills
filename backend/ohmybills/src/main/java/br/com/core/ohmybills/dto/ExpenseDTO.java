package br.com.core.ohmybills.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseDTO(UUID expenseId, String description, LocalDate firstPayDate, BigDecimal amount, Integer installments, Boolean isRecurring) {
}
