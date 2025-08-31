package br.com.core.ohmybills.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ExpenseDTO(
        UUID expenseId,
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate firstPayDate,
        BigDecimal amount,
        @Min(1)
        Integer installments,
        Boolean isRecurring,
        CreditCardDTO creditCard,
        List<TagDTO> tags) {
}
