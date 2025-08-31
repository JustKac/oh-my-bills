package br.com.core.ohmybills.service;

import java.util.Collections;
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
    private final TagServiceImpl tagService;

    public ExpenseServiceImpl(ExpenseRepository repository, UserServiceImpl userService, TagServiceImpl tagService) {
        super(repository);
        this.userService = userService;
        this.tagService = tagService;
    }

    @Override
    public PageResponseDTO<ExpenseDTO> listExpenses(UUID userId, int page, int size) {
        var result = repository.findAllByUserId(userId, PageRequest.of(page, size));
        return new PageResponseDTO<>(
                result.getContent().stream().map(expense -> new ExpenseDTO(
                        expense.getId(),
                        expense.getDescription(),
                        expense.getFirstPayDate(),
                        expense.getAmount(),
                        expense.getInstallments(),
                        expense.getIsRecurring(),
                        tagService.findByExpenseId(userId, expense.getId())
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
                expenseFound.getIsRecurring(),
                tagService.findByExpenseId(userId, expenseId)
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
                expenseToUpdate.getIsRecurring(),
                Collections.emptyList()
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

    @Override
    public void addTagToExpense(UUID userId, UUID expenseId, UUID tagId) {
        Expense expense = findByIdAndUserId(expenseId, userId);
        var tag = tagService.findByIdAndUserId(tagId, userId);
        expense.getTags().add(tag);
        save(expense);
    }

    @Override
    public void removeTagFromExpense(UUID userId, UUID expenseId, UUID tagId) {
        Expense expense = findByIdAndUserId(expenseId, userId);
        var tag = tagService.findByIdAndUserId(tagId, userId);
        expense.getTags().remove(tag);
        save(expense);
    }

    private Expense findByIdAndUserId(UUID id, UUID userId) {
        return repository.findByIdAndUserId(id, userId).orElseThrow(EntityNotFoundException::new);
    }
}
