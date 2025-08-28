package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.ExpenseDTO;
import br.com.core.ohmybills.dto.PageResponseDTO;


import java.util.List;
import java.util.UUID;

public interface ExpenseService {

    PageResponseDTO<ExpenseDTO> listExpenses(UUID userId, int page, int size);
    ExpenseDTO findExpenseById(UUID userId, UUID id);
    void addExpense(UUID userId, ExpenseDTO expenseDTO);
    ExpenseDTO updateExpense(UUID userId, ExpenseDTO expenseDTO);
    void deleteExpenseById(UUID userId, UUID id);
    void importExpenses(UUID userId, List<ExpenseDTO> expenses);

}
