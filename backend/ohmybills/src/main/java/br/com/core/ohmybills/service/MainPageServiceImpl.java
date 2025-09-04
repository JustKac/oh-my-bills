package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.MainPageDTO;
import br.com.core.ohmybills.model.Expense;
import br.com.core.ohmybills.model.Income;
import br.com.core.ohmybills.model.Tag;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MainPageServiceImpl implements MainPageService {

    private final IncomeServiceImpl incomeService;
    private final ExpenseServiceImpl expenseService;

    public MainPageServiceImpl (IncomeServiceImpl incomeService, ExpenseServiceImpl expenseService) {
        this.incomeService = incomeService;
        this.expenseService = expenseService;
    }

    @Override
    public MainPageDTO getMainPageInfo(UUID userId, YearMonth yearMonth) {
        List<Income> incomes = incomeService.findByUserIdAndFirstPayDateBefore(userId, yearMonth.atEndOfMonth());
        List<Expense> expenses = expenseService.findByUserIdAndFirstPayDateBefore(userId, yearMonth.atEndOfMonth());

        return new MainPageDTO(
                getTotalIncomeByYearMonth(incomes, yearMonth),
                getTotalExpenseByYearMonth(expenses, yearMonth),
                getTotalExpenseWithRecurrence(userId),
                getTotalExpenseByCreditCard(expenses, yearMonth),
                getTotalExpenseByTag(expenses, yearMonth));
    }

    @Override
    public BigDecimal getTotalIncomeByYearMonth(List<Income> incomes, YearMonth yearMonth) {

        BigDecimal total = BigDecimal.ZERO;

        for (Income income : incomes) {
            LocalDate start = income.getFirstPayDate();
            int installments = income.getInstallments();

            boolean isRecurring = income.getIsRecurring();
            boolean isApplies = isAppliesByInstallments(yearMonth, installments, start);

            if (isRecurring || isApplies) {
                total = total.add(income.getAmount());
            }
        }

        return total;
    }

    @Override
    public BigDecimal getTotalExpenseByYearMonth(List<Expense> expenses, YearMonth yearMonth) {

        BigDecimal total = BigDecimal.ZERO;

        for (Expense expense : expenses) {
            LocalDate start = expense.getFirstPayDate();
            int installments = expense.getInstallments();

            boolean isRecurring = expense.getIsRecurring();
            boolean appliesByInstallments = isAppliesByInstallments(yearMonth, installments, start);

            if (isRecurring || appliesByInstallments) {
                total = total.add(expense.getAmount());
            }
        }

        return total;
    }

    @Override
    public Map<String, BigDecimal> getTotalExpenseByCreditCard(List<Expense> expenses, YearMonth yearMonth) {
        Map<String, BigDecimal> expensesByCreditCard = new HashMap<>();

        for (Expense expense : expenses.stream().filter(expense -> expense.getCreditCard() != null).toList()) {

            String cardName = expense.getCreditCard().getName();
            LocalDate start = expense.getFirstPayDate();
            int installments = expense.getInstallments();

            boolean isRecurring = expense.getIsRecurring();
            boolean appliesByInstallments = isAppliesByInstallments(yearMonth, installments, start);

            if (isRecurring || appliesByInstallments) {
                expensesByCreditCard.merge(cardName, expense.getAmount(), BigDecimal::add);
            }
        }

        return expensesByCreditCard;
    }

    @Override
    public Map<String, BigDecimal> getTotalExpenseByTag(List<Expense> expenses, YearMonth yearMonth) {
        Map<String, BigDecimal> expensesByTags = new HashMap<>();

        for (Expense expense : expenses.stream().filter(expense -> expense.getTags() != null).toList()) {
            LocalDate start = expense.getFirstPayDate();
            int installments = expense.getInstallments();

            boolean isRecurring = expense.getIsRecurring();
            boolean appliesByInstallments = isAppliesByInstallments(yearMonth, installments, start);
            if (isRecurring || appliesByInstallments){
                applyTagsValues(expense, expensesByTags);
            }
        }

        return expensesByTags;
    }

    @Override
    public BigDecimal getTotalExpenseWithRecurrence(UUID userId) {
        List<Expense> expenses = expenseService.findAllRecurringExpenses(userId);
        return expenses.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static void applyTagsValues(Expense expense, Map<String, BigDecimal> expensesByTags) {
        for (Tag tag : expense.getTags()) {
            expensesByTags.merge(tag.getName(), expense.getAmount(), BigDecimal::add);
        }
    }

    private static boolean isAppliesByInstallments(YearMonth yearMonth, int installments, LocalDate start) {
        for (int i = 0; i < installments; i++) {
            LocalDate installmentDate = start.plusMonths(i);
            if (YearMonth.from(installmentDate).equals(yearMonth)) {
                return true;
            }
        }
        return false;
    }
}
