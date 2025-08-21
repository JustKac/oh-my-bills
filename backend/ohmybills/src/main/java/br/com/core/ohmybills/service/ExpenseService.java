package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.ExpenseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ExpenseService {

    ExpenseDTO findExpenseById(UUID expenseId);
    void addExpense(ExpenseDTO expenseDto);
    ExpenseDTO updateExpense(ExpenseDTO expenseDto);
    void deleteExpenseById(UUID expenseId);
    Page<ExpenseDTO> listExpenses(int page, int size);
    void importExpenses(List<ExpenseDTO> expenses);

}
