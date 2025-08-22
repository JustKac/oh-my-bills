package br.com.core.ohmybills.service;

import java.util.List;
import java.util.UUID;

import br.com.core.ohmybills.dto.IncomeDTO;
import br.com.core.ohmybills.dto.PageResponseDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.core.ohmybills.model.Income;
import br.com.core.ohmybills.repository.IncomeRepository;

@Service
public class IncomeServiceImpl extends GenericServiceImpl<Income, UUID, IncomeRepository> implements IncomeService {

    public IncomeServiceImpl(IncomeRepository repository) {
        super(repository);
    }

    @Override
    public IncomeDTO findIncomeById(UUID incomeId) {
        Income incomeFound = findById(incomeId);
        return new IncomeDTO(
                incomeFound.getId(),
                incomeFound.getDescription(),
                incomeFound.getFirstPayDate(),
                incomeFound.getAmount(),
                incomeFound.getInstallments(),
                incomeFound.getIsRecurring()
        );
    }

    @Override
    public void addIncome(IncomeDTO incomeDTO) {
        save(new Income()
                .setDescription(incomeDTO.description())
                .setFirstPayDate(incomeDTO.firstPayDate())
                .setAmount(incomeDTO.amount())
                .setInstallments(incomeDTO.installments())
                .setIsRecurring(incomeDTO.isRecurring())
        );
    }

    @Override
    public IncomeDTO updateIncome(IncomeDTO incomeDTO) {
        Income incomeToUpdate = findById(incomeDTO.incomeId());
        incomeToUpdate.setDescription(incomeDTO.description())
                .setFirstPayDate(incomeDTO.firstPayDate())
                .setAmount(incomeDTO.amount())
                .setInstallments(incomeDTO.installments())
                .setIsRecurring(incomeDTO.isRecurring());

        save(incomeToUpdate);
        return new IncomeDTO(
                incomeToUpdate.getId(),
                incomeToUpdate.getDescription(),
                incomeToUpdate.getFirstPayDate(),
                incomeToUpdate.getAmount(),
                incomeToUpdate.getInstallments(),
                incomeToUpdate.getIsRecurring()
        );
    }

    @Override
    public void deleteIncomeById(UUID incomeId) {
        deleteById(incomeId);
    }

    @Override
    public PageResponseDTO<IncomeDTO> listIncomes(int page, int size) {

        var result = repository.findAll(PageRequest.of(page, size));
        return new PageResponseDTO<IncomeDTO>(
                result.getContent().stream().map(income -> new IncomeDTO(
                        income.getId(),
                        income.getDescription(),
                        income.getFirstPayDate(),
                        income.getAmount(),
                        income.getInstallments(),
                        income.getIsRecurring()
                )).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast()
        );

    }

    @Override
    public void importIncomes(List<IncomeDTO> incomes) {
        saveAll(incomes.stream().map(incomeDTO ->
            new Income()
                .setDescription(incomeDTO.description())
                .setFirstPayDate(incomeDTO.firstPayDate())
                .setAmount(incomeDTO.amount())
                .setInstallments(incomeDTO.installments())
                .setIsRecurring(incomeDTO.isRecurring())
        ).toList());
    }
}
