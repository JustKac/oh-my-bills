package br.com.core.ohmybills.controller;

import br.com.core.ohmybills.dto.IncomeDTO;
import br.com.core.ohmybills.dto.PageResponseDTO;
import br.com.core.ohmybills.security.CurrentUser;
import br.com.core.ohmybills.security.UserContext;
import br.com.core.ohmybills.service.IncomeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public PageResponseDTO<IncomeDTO> list(@CurrentUser UserContext user,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        return incomeService.listIncomes(user.userId(), page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public IncomeDTO getById(@CurrentUser UserContext user, @PathVariable UUID id) {
        return incomeService.findIncomeById(user.userId(), id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Void> create(@CurrentUser UserContext user, @RequestBody @Valid IncomeDTO incomeDTO) {
        incomeService.addIncome(user.userId(), incomeDTO);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public IncomeDTO update(@CurrentUser UserContext user, @RequestBody @Valid IncomeDTO incomeDTO) {
        return incomeService.updateIncome(user.userId(), incomeDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Void> delete(@CurrentUser UserContext user, @PathVariable UUID id) {
        incomeService.deleteIncomeById(user.userId(), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/import")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Void> bulkImport(@CurrentUser UserContext user, @RequestBody @Valid List<IncomeDTO> incomes) {
        incomeService.importIncomes(user.userId(), incomes);
        return ResponseEntity.accepted().build();
    }
}