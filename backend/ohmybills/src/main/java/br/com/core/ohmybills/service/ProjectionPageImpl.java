package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.ProjectionDTO;
import br.com.core.ohmybills.model.Expense;
import br.com.core.ohmybills.model.Income;
import br.com.core.ohmybills.utils.RecurrenceAndInstallmentsUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectionPageImpl implements ProjectionPage {

    private final IncomeServiceImpl incomeService;
    private final ExpenseServiceImpl expenseService;
    private static final int MAX_PROJECTION_MONTHS = 12;

    public ProjectionPageImpl(IncomeServiceImpl incomeService, ExpenseServiceImpl expenseService) {
        this.incomeService = incomeService;
        this.expenseService = expenseService;
    }

    @Override
    public List<ProjectionDTO> getFinancialProjection(UUID userId, YearMonth startMonth, int months) {
        int monthsToProject = Math.min(months, MAX_PROJECTION_MONTHS);
        LocalDate endDate = startMonth.plusMonths(monthsToProject).atDay(1);

        List<Income> incomes = incomeService.findByUserIdAndFirstPayDateBefore(userId, endDate);
        List<Expense> expenses = expenseService.findByUserIdAndFirstPayDateBefore(userId, endDate);

        return generateProjections(startMonth, monthsToProject, incomes, expenses);
    }

    @Override
    public List<ProjectionDTO> getProjectionFilteredByTags(UUID userId, YearMonth startMonth, int months, List<UUID> tagIds) {
        int monthsToProject = Math.min(months, MAX_PROJECTION_MONTHS);
        LocalDate endDate = startMonth.plusMonths(monthsToProject).atDay(1);

        List<Income> incomes = incomeService.findByUserIdAndFirstPayDateBefore(userId, endDate);
        List<Expense> allExpenses = expenseService.findByUserIdAndFirstPayDateBefore(userId, endDate);
        List<Expense> filteredExpenses = filterExpensesByTags(allExpenses, tagIds);

        return generateProjections(startMonth, monthsToProject, incomes, filteredExpenses);
    }

    public List<ProjectionDTO> getProjectionFilteredByCards(UUID userId, YearMonth startMonth, int months, List<UUID> cardIds) {
        int monthsToProject = Math.min(months, MAX_PROJECTION_MONTHS);
        LocalDate endDate = startMonth.plusMonths(monthsToProject).atDay(1);

        List<Income> incomes = incomeService.findByUserIdAndFirstPayDateBefore(userId, endDate);
        List<Expense> allExpenses = expenseService.findByUserIdAndFirstPayDateBefore(userId, endDate);
        List<Expense> filteredExpenses = filterExpensesByCards(allExpenses, cardIds);

        return generateProjections(startMonth, monthsToProject, incomes, filteredExpenses);
    }

    public List<ProjectionDTO> getProjectionFilteredByTagsAndCards(
            UUID userId, YearMonth startMonth, int months, List<UUID> tagIds, List<UUID> cardIds) {
        int monthsToProject = Math.min(months, MAX_PROJECTION_MONTHS);
        LocalDate endDate = startMonth.plusMonths(monthsToProject).atDay(1);

        List<Income> incomes = incomeService.findByUserIdAndFirstPayDateBefore(userId, endDate);
        List<Expense> allExpenses = expenseService.findByUserIdAndFirstPayDateBefore(userId, endDate);

        // Aplicar os dois filtros em sequência
        List<Expense> filteredExpenses = filterExpensesByCards(
                filterExpensesByTags(allExpenses, tagIds),
                cardIds
        );

        return generateProjections(startMonth, monthsToProject, incomes, filteredExpenses);
    }

    private List<ProjectionDTO> generateProjections(
            YearMonth startMonth, int monthsToProject, List<Income> incomes, List<Expense> expenses) {

        List<ProjectionDTO> projections = new ArrayList<>();
        YearMonth currentMonth = startMonth;
        BigDecimal accumulatedBalance = BigDecimal.ZERO;

        for (int i = 0; i < monthsToProject; i++) {
            // Calcular totais para o mês atual
            BigDecimal monthlyIncome = calculateMonthlyTotal(incomes, currentMonth);
            BigDecimal monthlyExpense = calculateMonthlyTotal(expenses, currentMonth);
            BigDecimal monthlyBalance = monthlyIncome.subtract(monthlyExpense);
            accumulatedBalance = accumulatedBalance.add(monthlyBalance);

            // Calcular despesas por categoria
            Map<String, BigDecimal> expensesByTag = calculateExpensesByCategory(expenses, currentMonth, true);
            Map<String, BigDecimal> expensesByCard = calculateExpensesByCategory(expenses, currentMonth, false);

            // Criar DTO de projeção
            projections.add(new ProjectionDTO(
                    currentMonth,
                    monthlyIncome,
                    monthlyExpense,
                    monthlyBalance,
                    accumulatedBalance,
                    expensesByTag,
                    expensesByCard
            ));

            // Avançar para o próximo mês
            currentMonth = currentMonth.plusMonths(1);
        }

        return projections;
    }

    /**
     * Calcula o total mensal para receitas ou despesas.
     */
    private <T> BigDecimal calculateMonthlyTotal(List<T> items, YearMonth yearMonth) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;

        if (items.get(0) instanceof Income) {
            for (T item : items) {
                Income income = (Income) item;
                if (shouldApplyIncome(income, yearMonth)) {
                    total = total.add(income.getAmount());
                }
            }
        } else if (items.get(0) instanceof Expense) {
            for (T item : items) {
                Expense expense = (Expense) item;
                if (!expense.getIsArchived() && shouldApplyExpense(expense, yearMonth)) {
                    total = total.add(expense.getAmount());
                }
            }
        }

        return total;
    }

    /**
     * Calcula despesas por categoria (tag ou cartão).
     *
     * @param byTag true para calcular por tag, false para calcular por cartão
     */
    private Map<String, BigDecimal> calculateExpensesByCategory(
            List<Expense> expenses, YearMonth yearMonth, boolean byTag) {

        Map<String, BigDecimal> result = new HashMap<>();

        for (Expense expense : expenses) {
            if (expense.getIsArchived() || !shouldApplyExpense(expense, yearMonth)) {
                continue;
            }

            if (byTag) {
                // Agrupar por tag
                if (expense.getTags() != null && !expense.getTags().isEmpty()) {
                    expense.getTags().forEach(tag ->
                            result.merge(tag.getName(), expense.getAmount(), BigDecimal::add)
                    );
                }
            } else {
                // Agrupar por cartão
                if (expense.getCreditCard() != null) {
                    String cardName = expense.getCreditCard().getName();
                    result.merge(cardName, expense.getAmount(), BigDecimal::add);
                }
            }
        }

        return result;
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

    private List<Expense> filterExpensesByTags(List<Expense> expenses, List<UUID> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return expenses;
        }

        return expenses.stream()
                .filter(expense -> expense.getTags() != null &&
                        !expense.getTags().isEmpty() &&
                        expense.getTags().stream()
                                .anyMatch(tag -> tagIds.contains(tag.getId())))
                .collect(Collectors.toList());
    }

    private List<Expense> filterExpensesByCards(List<Expense> expenses, List<UUID> cardIds) {
        if (cardIds == null || cardIds.isEmpty()) {
            return expenses;
        }

        return expenses.stream()
                .filter(expense -> expense.getCreditCard() != null &&
                        cardIds.contains(expense.getCreditCard().getId()))
                .collect(Collectors.toList());
    }
}