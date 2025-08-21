package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.ExpenseDTO;
import br.com.core.ohmybills.dto.IncomeDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface IncomeService {

    IncomeDTO findExpenseById(UUID incomeId);
    void addExpense(IncomeDTO incomeDTO);
    IncomeDTO updateExpense(IncomeDTO incomeDTO);
    void deleteExpenseById(UUID incomeId);
    Page<IncomeDTO> listExpenses(int page, int size);
    void importExpenses(List<IncomeDTO> incomes);

}
