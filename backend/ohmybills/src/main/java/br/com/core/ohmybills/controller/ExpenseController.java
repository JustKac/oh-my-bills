package br.com.core.ohmybills.controller;

import br.com.core.ohmybills.dto.ExpenseDTO;
import br.com.core.ohmybills.dto.PageResponseDTO;
import br.com.core.ohmybills.security.CurrentUser;
import br.com.core.ohmybills.security.UserContext;
import br.com.core.ohmybills.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public PageResponseDTO<ExpenseDTO> list(@CurrentUser UserContext user,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        return expenseService.listExpenses(user.userId(), page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ExpenseDTO getById(@CurrentUser UserContext user, @PathVariable UUID id) {
        return expenseService.findExpenseById(user.userId(), id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Void> create(@CurrentUser UserContext user, @RequestBody @Valid ExpenseDTO expenseDTO) {
        expenseService.addExpense(user.userId(), expenseDTO);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ExpenseDTO update(@CurrentUser UserContext user, @PathVariable UUID id, @RequestBody @Valid ExpenseDTO expenseDTO) {
        var toUpdate = new ExpenseDTO(id, expenseDTO.description(), expenseDTO.firstPayDate(),
                expenseDTO.amount(), expenseDTO.installments(), expenseDTO.isRecurring()
        );
        return expenseService.updateExpense(user.userId(), toUpdate);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Void> delete(@CurrentUser UserContext user, @PathVariable UUID id) {
        expenseService.deleteExpenseById(user.userId(), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/import")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Void> bulkImport(@CurrentUser UserContext user, @RequestBody @Valid List<ExpenseDTO> expenses) {
        expenseService.importExpenses(user.userId(), expenses);
        return ResponseEntity.accepted().build();
    }
}