package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.MainPageDTO;
import br.com.core.ohmybills.model.Expense;
import br.com.core.ohmybills.model.Income;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MainPageService {

    MainPageDTO getMainPageInfo(UUID userId, YearMonth yearMonth);
    BigDecimal getTotalIncomeByYearMonth(List<Income> incomes, YearMonth yearMonth);
    BigDecimal getTotalExpenseByYearMonth(List<Expense> expenses, YearMonth yearMonth);
    Map<String, BigDecimal> getTotalExpenseByCreditCard(List<Expense> expenses, YearMonth yearMonth);
    Map<String, BigDecimal> getTotalExpenseByTag(List<Expense> expenses, YearMonth yearMonth);
    BigDecimal getTotalExpenseWithRecurrence(UUID userId, YearMonth yearMonth);
}
