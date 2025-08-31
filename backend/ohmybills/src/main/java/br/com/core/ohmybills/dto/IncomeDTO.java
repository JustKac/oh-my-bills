package br.com.core.ohmybills.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record IncomeDTO(
        UUID incomeId,
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate firstPayDate,
        BigDecimal amount,
        Integer installments,
        Boolean isRecurring) {
}
