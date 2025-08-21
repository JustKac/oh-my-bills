package br.com.core.ohmybills.service;

import java.util.List;
import java.util.UUID;

import br.com.core.ohmybills.dto.ExpenseDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import br.com.core.ohmybills.model.Expense;
import br.com.core.ohmybills.repository.ExpenseRepository;

@Service
public class ExpenseServiceImpl extends GenericServiceImpl<Expense, UUID, ExpenseRepository> implements ExpenseService {

    @Override
    public ExpenseDTO findExpenseById(UUID expenseId) {
        return null;
    }

    @Override
    public void addExpense(ExpenseDTO expenseDto) {

    }

    @Override
    public ExpenseDTO updateExpense(ExpenseDTO expenseDto) {
        return null;
    }

    @Override
    public void deleteExpenseById(UUID expenseId) {

    }

    @Override
    public Page<ExpenseDTO> listExpenses(int page, int size) {
        return null;
    }

    @Override
    public void importExpenses(List<ExpenseDTO> expenses) {

    }

    public ExpenseServiceImpl(ExpenseRepository repository) {
        super(repository);
    }

}
