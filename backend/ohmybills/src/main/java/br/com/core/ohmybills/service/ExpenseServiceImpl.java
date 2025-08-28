package br.com.core.ohmybills.service;

import java.util.List;
import java.util.UUID;

import br.com.core.ohmybills.dto.ExpenseDTO;
import br.com.core.ohmybills.dto.PageResponseDTO;
import br.com.core.ohmybills.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.core.ohmybills.model.Expense;
import br.com.core.ohmybills.repository.ExpenseRepository;

@Service
public class ExpenseServiceImpl extends GenericServiceImpl<Expense, UUID, ExpenseRepository> implements ExpenseService {

    private final UserServiceImpl userService;

    public ExpenseServiceImpl(ExpenseRepository repository, UserServiceImpl userService) {
        super(repository);
        this.userService = userService;
    }

    @Override
    public PageResponseDTO<ExpenseDTO> listExpenses(UUID userId, int page, int size) {
        var result = repository.findAllByUserId(userId, PageRequest.of(page, size));
        return new PageResponseDTO<ExpenseDTO>(
                result.getContent().stream().map(expense -> new ExpenseDTO(
                        expense.getId(),
                        expense.getDescription(),
                        expense.getFirstPayDate(),
                        expense.getAmount(),
                        expense.getInstallments(),
                        expense.getIsRecurring()
                )).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast()
        );
    }

    @Override
    public ExpenseDTO findExpenseById(UUID userId, UUID expenseId) {
        Expense expenseFound = findByIdAndUserId(expenseId, userId);
        return new ExpenseDTO(
                expenseFound.getId(),
                expenseFound.getDescription(),
                expenseFound.getFirstPayDate(),
                expenseFound.getAmount(),
                expenseFound.getInstallments(),
                expenseFound.getIsRecurring()
        );
    }

    @Override
    public void addExpense(UUID userId, ExpenseDTO expenseDTO) {
        save(new Expense()
                .setDescription(expenseDTO.description())
                .setFirstPayDate(expenseDTO.firstPayDate())
                .setAmount(expenseDTO.amount())
                .setInstallments(expenseDTO.installments())
                .setIsRecurring(expenseDTO.isRecurring())
                .setUser(userService.findById(userId))
        );
    }

    @Override
    public ExpenseDTO updateExpense(UUID userId, ExpenseDTO expenseDTO) {
        Expense expenseToUpdate = findByIdAndUserId(expenseDTO.expenseId(), userId);
        expenseToUpdate.setDescription(expenseDTO.description())
                .setFirstPayDate(expenseDTO.firstPayDate())
                .setAmount(expenseDTO.amount())
                .setInstallments(expenseDTO.installments())
                .setIsRecurring(expenseDTO.isRecurring());

        save(expenseToUpdate);
        return new ExpenseDTO(
                expenseToUpdate.getId(),
                expenseToUpdate.getDescription(),
                expenseToUpdate.getFirstPayDate(),
                expenseToUpdate.getAmount(),
                expenseToUpdate.getInstallments(),
                expenseToUpdate.getIsRecurring()
        );
    }

    @Override
    public void deleteExpenseById(UUID userId, UUID expenseId) {
        findByIdAndUserId(expenseId, userId);
        deleteById(expenseId);
    }

    @Override
    public void importExpenses(UUID userId, List<ExpenseDTO> expenses) {
        User userFound = userService.findById(userId);
        saveAll(expenses.stream().map(expenseDTO ->
                new Expense()
                        .setDescription(expenseDTO.description())
                        .setFirstPayDate(expenseDTO.firstPayDate())
                        .setAmount(expenseDTO.amount())
                        .setInstallments(expenseDTO.installments())
                        .setIsRecurring(expenseDTO.isRecurring())
                        .setUser(userFound)
        ).toList());
    }

    private Expense findByIdAndUserId(UUID id, UUID userId) {
        return repository.findByIdAndUserId(id, userId).orElseThrow(EntityNotFoundException::new);
    }
}
