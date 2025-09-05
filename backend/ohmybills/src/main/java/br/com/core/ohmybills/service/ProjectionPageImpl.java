package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.ProjectionDTO;
import br.com.core.ohmybills.model.Expense;
import br.com.core.ohmybills.model.Income;
import br.com.core.ohmybills.model.Tag;
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
        // Validar número de meses
        int monthsToProject = Math.min(months, MAX_PROJECTION_MONTHS);

        // Buscar todos os dados necessários
        LocalDate endDate = startMonth.plusMonths(monthsToProject).atDay(1);
        List<Income> incomes = incomeService.findByUserIdAndFirstPayDateBefore(userId, endDate);
        List<Expense> expenses = expenseService.findByUserIdAndFirstPayDateBefore(userId, endDate);

        // Criar projeções para cada mês
        return generateProjections(startMonth, monthsToProject, incomes, expenses);
    }

    @Override
    public List<ProjectionDTO> getProjectionFilteredByTags(UUID userId, YearMonth startMonth, int months, List<UUID> tagIds) {
        // Validar número de meses
        int monthsToProject = Math.min(months, MAX_PROJECTION_MONTHS);

        // Buscar todos os dados necessários
        LocalDate endDate = startMonth.plusMonths(monthsToProject).atDay(1);
        List<Income> incomes = incomeService.findByUserIdAndFirstPayDateBefore(userId, endDate);

        // Buscar apenas despesas com as tags selecionadas
        List<Expense> allExpenses = expenseService.findByUserIdAndFirstPayDateBefore(userId, endDate);
        List<Expense> filteredExpenses = filterExpensesByTags(allExpenses, tagIds);

        // Criar projeções para cada mês
        return generateProjections(startMonth, monthsToProject, incomes, filteredExpenses);
    }

    /**
     * Obtém a projeção financeira filtrada por cartões de crédito
     *
     * @param userId ID do usuário
     * @param startMonth Mês inicial da projeção
     * @param months Número de meses a projetar
     * @param cardIds IDs dos cartões para filtrar
     * @return Lista de projeções mensais filtradas
     */
    public List<ProjectionDTO> getProjectionFilteredByCards(UUID userId, YearMonth startMonth, int months, List<UUID> cardIds) {
        // Validar número de meses
        int monthsToProject = Math.min(months, MAX_PROJECTION_MONTHS);

        // Buscar todos os dados necessários
        LocalDate endDate = startMonth.plusMonths(monthsToProject).atDay(1);
        List<Income> incomes = incomeService.findByUserIdAndFirstPayDateBefore(userId, endDate);

        // Buscar apenas despesas com os cartões selecionados
        List<Expense> allExpenses = expenseService.findByUserIdAndFirstPayDateBefore(userId, endDate);
        List<Expense> filteredExpenses = filterExpensesByCards(allExpenses, cardIds);

        // Criar projeções para cada mês
        return generateProjections(startMonth, monthsToProject, incomes, filteredExpenses);
    }

    /**
     * Obtém a projeção financeira filtrada por tags e cartões de crédito
     *
     * @param userId ID do usuário
     * @param startMonth Mês inicial da projeção
     * @param months Número de meses a projetar
     * @param tagIds IDs das tags para filtrar
     * @param cardIds IDs dos cartões para filtrar
     * @return Lista de projeções mensais filtradas
     */
    public List<ProjectionDTO> getProjectionFilteredByTagsAndCards(
            UUID userId, YearMonth startMonth, int months, List<UUID> tagIds, List<UUID> cardIds) {
        // Validar número de meses
        int monthsToProject = Math.min(months, MAX_PROJECTION_MONTHS);

        // Buscar todos os dados necessários
        LocalDate endDate = startMonth.plusMonths(monthsToProject).atDay(1);
        List<Income> incomes = incomeService.findByUserIdAndFirstPayDateBefore(userId, endDate);

        // Buscar apenas despesas com as tags e cartões selecionados
        List<Expense> allExpenses = expenseService.findByUserIdAndFirstPayDateBefore(userId, endDate);
        List<Expense> filteredByTags = filterExpensesByTags(allExpenses, tagIds);
        List<Expense> filteredExpenses = filterExpensesByCards(filteredByTags, cardIds);

        // Criar projeções para cada mês
        return generateProjections(startMonth, monthsToProject, incomes, filteredExpenses);
    }

    private List<ProjectionDTO> generateProjections(
            YearMonth startMonth, int monthsToProject, List<Income> incomes, List<Expense> expenses) {
        List<ProjectionDTO> projections = new ArrayList<>();
        YearMonth currentMonth = startMonth;
        BigDecimal accumulatedBalance = BigDecimal.ZERO;

        for (int i = 0; i < monthsToProject; i++) {
            BigDecimal monthlyIncome = calculateMonthlyIncome(incomes, currentMonth);
            BigDecimal monthlyExpense = calculateMonthlyExpense(expenses, currentMonth);
            BigDecimal monthlyBalance = monthlyIncome.subtract(monthlyExpense);
            accumulatedBalance = accumulatedBalance.add(monthlyBalance);

            Map<String, BigDecimal> expensesByTag = calculateExpensesByTag(expenses, currentMonth);
            Map<String, BigDecimal> expensesByCard = calculateExpensesByCard(expenses, currentMonth);

            projections.add(new ProjectionDTO(
                    currentMonth,
                    monthlyIncome,
                    monthlyExpense,
                    monthlyBalance,
                    accumulatedBalance,
                    expensesByTag,
                    expensesByCard
            ));

            currentMonth = currentMonth.plusMonths(1);
        }

        return projections;
    }

    private BigDecimal calculateMonthlyIncome(List<Income> incomes, YearMonth yearMonth) {
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

    private BigDecimal calculateMonthlyExpense(List<Expense> expenses, YearMonth yearMonth) {
        BigDecimal total = BigDecimal.ZERO;

        for (Expense expense : expenses) {
            // Ignorar despesas arquivadas
            if (expense.getIsArchived()) {
                continue;
            }

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

    private Map<String, BigDecimal> calculateExpensesByTag(List<Expense> expenses, YearMonth yearMonth) {
        Map<String, BigDecimal> expensesByTag = new HashMap<>();

        for (Expense expense : expenses.stream()
                .filter(e -> !e.getIsArchived() && e.getTags() != null && !e.getTags().isEmpty())
                .toList()) {

            LocalDate start = expense.getFirstPayDate();
            int installments = expense.getInstallments();

            boolean isRecurring = expense.getIsRecurring();
            boolean appliesByInstallments = isAppliesByInstallments(yearMonth, installments, start);

            if (isRecurring || appliesByInstallments) {
                for (Tag tag : expense.getTags()) {
                    String tagName = tag.getName();
                    expensesByTag.merge(tagName, expense.getAmount(), BigDecimal::add);
                }
            }
        }

        return expensesByTag;
    }

    private Map<String, BigDecimal> calculateExpensesByCard(List<Expense> expenses, YearMonth yearMonth) {
        Map<String, BigDecimal> expensesByCard = new HashMap<>();

        for (Expense expense : expenses.stream()
                .filter(e -> !e.getIsArchived() && e.getCreditCard() != null)
                .toList()) {

            String cardName = expense.getCreditCard().getName();
            LocalDate start = expense.getFirstPayDate();
            int installments = expense.getInstallments();

            boolean isRecurring = expense.getIsRecurring();
            boolean appliesByInstallments = isAppliesByInstallments(yearMonth, installments, start);

            if (isRecurring || appliesByInstallments) {
                expensesByCard.merge(cardName, expense.getAmount(), BigDecimal::add);
            }
        }

        return expensesByCard;
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