package br.com.core.ohmybills.controller;

import br.com.core.ohmybills.dto.IncomeDTO;
import br.com.core.ohmybills.dto.PageResponseDTO;
import br.com.core.ohmybills.service.IncomeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public PageResponseDTO<IncomeDTO> list(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        return incomeService.listIncomes(page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public IncomeDTO getById(@PathVariable UUID id) {
        return incomeService.findIncomeById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Void> create(@RequestBody @Valid IncomeDTO incomeDTO) {
        incomeService.addIncome(incomeDTO);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public IncomeDTO update(@PathVariable UUID id, @RequestBody @Valid IncomeDTO incomeDTO) {
        var toUpdate = new IncomeDTO(
                id,
                incomeDTO.description(),
                incomeDTO.firstPayDate(),
                incomeDTO.amount(),
                incomeDTO.installments(),
                incomeDTO.isRecurring()
        );
        return incomeService.updateIncome(toUpdate);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        incomeService.deleteIncomeById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/import")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Void> bulkImport(@RequestBody @Valid List<IncomeDTO> incomes) {
        incomeService.importIncomes(incomes);
        return ResponseEntity.accepted().build();
    }
}