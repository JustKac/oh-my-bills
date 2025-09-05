package br.com.core.ohmybills.dto;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Map;

public record ProjectionDTO(
        YearMonth month,
        BigDecimal income,
        BigDecimal expense,
        BigDecimal balance,
        BigDecimal accumulatedBalance,
        Map<String, BigDecimal> expensesByTag,
        Map<String, BigDecimal> expensesByCard
) {}