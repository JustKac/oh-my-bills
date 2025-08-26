package br.com.core.ohmybills.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.core.ohmybills.dto.IncomeDTO;
import br.com.core.ohmybills.dto.PageResponseDTO;
import br.com.core.ohmybills.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.core.ohmybills.model.Income;
import br.com.core.ohmybills.repository.IncomeRepository;

@Service
public class IncomeServiceImpl extends GenericServiceImpl<Income, UUID, IncomeRepository> implements IncomeService {

    private final UserServiceImpl userService;

    public IncomeServiceImpl(IncomeRepository repository, UserServiceImpl userService) {
        super(repository);
        this.userService = userService;
    }

    @Override
    public IncomeDTO findIncomeById(UUID userId, UUID incomeId) {
        Income incomeFound = findByIdAndUserId(incomeId, userId);
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
    public void addIncome(UUID userId, IncomeDTO incomeDTO) {
        save(new Income()
                .setDescription(incomeDTO.description())
                .setFirstPayDate(incomeDTO.firstPayDate())
                .setAmount(incomeDTO.amount())
                .setInstallments(incomeDTO.installments())
                .setIsRecurring(incomeDTO.isRecurring())
                .setUser(userService.findById(userId))
        );
    }

    @Override
    public IncomeDTO updateIncome(UUID userId, IncomeDTO incomeDTO) {
        Income incomeToUpdate = findByIdAndUserId(incomeDTO.incomeId(), userId);
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
    public void deleteIncomeById(UUID userId, UUID incomeId) {
        findByIdAndUserId(incomeId, userId);
        deleteById(incomeId);
    }

    @Override
    public PageResponseDTO<IncomeDTO> listIncomes(UUID userId, int page, int size) {

        var result = repository.findAllByUserId(userId, PageRequest.of(page, size));
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
    public void importIncomes(UUID userId, List<IncomeDTO> incomes) {
        User userFound = userService.findById(userId);
        saveAll(incomes.stream().map(incomeDTO ->
            new Income()
                .setDescription(incomeDTO.description())
                .setFirstPayDate(incomeDTO.firstPayDate())
                .setAmount(incomeDTO.amount())
                .setInstallments(incomeDTO.installments())
                .setIsRecurring(incomeDTO.isRecurring())
                    .setUser(userFound)
        ).toList());
    }

    private Income findByIdAndUserId(UUID id, UUID userId) {
        return repository.findByIdAndUserId(id, userId).orElseThrow(EntityNotFoundException::new);
    }
}
