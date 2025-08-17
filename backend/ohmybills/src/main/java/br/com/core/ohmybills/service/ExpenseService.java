package br.com.core.ohmybills.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.core.ohmybills.model.Expense;
import br.com.core.ohmybills.repository.ExpenseRepository;

@Service
public class ExpenseService extends GenericServiceImpl<Expense, UUID, ExpenseRepository> {

    public ExpenseService(ExpenseRepository repository) {
        super(repository);
    }

}
