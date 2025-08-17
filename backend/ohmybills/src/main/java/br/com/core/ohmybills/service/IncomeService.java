package br.com.core.ohmybills.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.core.ohmybills.model.Income;
import br.com.core.ohmybills.repository.IncomeRepository;

@Service
public class IncomeService extends GenericServiceImpl<Income, UUID, IncomeRepository> {

    public IncomeService(IncomeRepository repository) {
        super(repository);
    }

}
