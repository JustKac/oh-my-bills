package br.com.core.ohmybills.service.impl;

import br.com.core.ohmybills.dto.MainPageDTO;
import br.com.core.ohmybills.model.Expense;
import br.com.core.ohmybills.model.Income;
import br.com.core.ohmybills.service.MainPageService;
import br.com.core.ohmybills.utils.RecurrenceAndInstallmentsUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MainPageServiceImpl implements MainPageService {

    private final IncomeServiceImpl incomeService;
    private final ExpenseServiceImpl expenseService;

    public MainPageServiceImpl(IncomeServiceImpl incomeService, ExpenseServiceImpl expenseService) {
        this.incomeService = incomeService;
        this.expenseService = expenseService;
    }

    @Override
    public MainPageDTO getMainPageInfo(UUID userId, YearMonth yearMonth) {
        List<Income> incomes = incomeService.findByUserIdAndFirstPayDateBefore(userId, yearMonth.atEndOfMonth().plusDays(1));
        List<Expense> expenses = expenseService.findByUserIdAndFirstPayDateBefore(userId, yearMonth.atEndOfMonth().plusDays(1));

        return new MainPageDTO(
                getTotalIncomeByYearMonth(incomes, yearMonth),
                getTotalExpenseByYearMonth(expenses, yearMonth),
                getTotalExpenseWithRecurrence(userId, yearMonth),
                getTotalExpenseByCreditCard(expenses, yearMonth),
                getTotalExpenseByTag(expenses, yearMonth)
        );
    }

    @Override
    public BigDecimal getTotalIncomeByYearMonth(List<Income> incomes, YearMonth yearMonth) {
        return incomes.stream()
                .filter(income -> shouldApplyIncome(income, yearMonth))
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalExpenseByYearMonth(List<Expense> expenses, YearMonth yearMonth) {
        return expenses.stream()
                .filter(expense -> !expense.getIsArchived())
                .filter(expense -> shouldApplyExpense(expense, yearMonth))
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Map<String, BigDecimal> getTotalExpenseByCreditCard(List<Expense> expenses, YearMonth yearMonth) {
        return expenses.stream()
                .filter(expense -> !expense.getIsArchived())
                .filter(expense -> expense.getCreditCard() != null)
                .filter(expense -> shouldApplyExpense(expense, yearMonth))
                .collect(Collectors.groupingBy(
                        expense -> expense.getCreditCard().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));
    }

    @Override
    public Map<String, BigDecimal> getTotalExpenseByTag(List<Expense> expenses, YearMonth yearMonth) {
        Map<String, BigDecimal> expensesByTags = new HashMap<>();

        expenses.stream()
                .filter(expense -> !expense.getIsArchived())
                .filter(expense -> expense.getTags() != null && !expense.getTags().isEmpty())
                .filter(expense -> shouldApplyExpense(expense, yearMonth))
                .forEach(expense -> applyTagsValues(expense, expensesByTags));

        return expensesByTags;
    }

    @Override
    public BigDecimal getTotalExpenseWithRecurrence(UUID userId, YearMonth yearMonth) {
        List<Expense> expenses = expenseService.findAllRecurringExpenses(userId, yearMonth.atEndOfMonth());
        return expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean shouldApplyIncome(Income income, YearMonth yearMonth) {
        LocalDate startDate = income.getFirstPayDate();

        // Verifica se a data de início está no mês ou antes dele
        if (!RecurrenceAndInstallmentsUtils.isDateInOrBeforeMonth(yearMonth, startDate)) {
            return false;
        }

        return income.getIsRecurring() ||
                RecurrenceAndInstallmentsUtils.isAnyInstallmentInMonth(yearMonth, income.getInstallments(), startDate);
    }

    private boolean shouldApplyExpense(Expense expense, YearMonth yearMonth) {
        LocalDate startDate = expense.getFirstPayDate();

        // Verifica se a data de início está no mês ou antes dele
        if (!RecurrenceAndInstallmentsUtils.isDateInOrBeforeMonth(yearMonth, startDate)) {
            return false;
        }

        return expense.getIsRecurring() ||
                RecurrenceAndInstallmentsUtils.isAnyInstallmentInMonth(yearMonth, expense.getInstallments(), startDate);
    }

    private static void applyTagsValues(Expense expense, Map<String, BigDecimal> expensesByTags) {
        expense.getTags().forEach(tag ->
                expensesByTags.merge(tag.getName(), expense.getAmount(), BigDecimal::add)
        );
    }
}
