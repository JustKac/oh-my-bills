package br.com.core.ohmybills.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreditCardDTO(
        UUID creditCardId,
        String name,
        @Pattern(regexp = "\\d{4}", message = "Last four digits must be exactly 4 digits")
        String lastFourDigits,
        String brand,
        BigDecimal creditLimit,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dueDate,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate bestShoppingDay
) {
}