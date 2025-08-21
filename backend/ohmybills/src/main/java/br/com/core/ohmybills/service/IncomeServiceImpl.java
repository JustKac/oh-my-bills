package br.com.core.ohmybills.service;

import java.util.List;
import java.util.UUID;

import br.com.core.ohmybills.dto.IncomeDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import br.com.core.ohmybills.model.Income;
import br.com.core.ohmybills.repository.IncomeRepository;

@Service
public class IncomeServiceImpl extends GenericServiceImpl<Income, UUID, IncomeRepository> implements IncomeService {

    public IncomeServiceImpl(IncomeRepository repository) {
        super(repository);
    }

    @Override
    public IncomeDTO findExpenseById(UUID incomeId) {
        return null;
    }

    @Override
    public void addExpense(IncomeDTO incomeDTO) {

    }

    @Override
    public IncomeDTO updateExpense(IncomeDTO incomeDTO) {
        return null;
    }

    @Override
    public void deleteExpenseById(UUID incomeId) {

    }

    @Override
    public Page<IncomeDTO> listExpenses(int page, int size) {
        return null;
    }

    @Override
    public void importExpenses(List<IncomeDTO> incomes) {

    }
}
