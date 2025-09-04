package br.com.core.ohmybills.controller;

import br.com.core.ohmybills.dto.PageResponseDTO;
import br.com.core.ohmybills.dto.CreditCardDTO;
import br.com.core.ohmybills.security.CurrentUser;
import br.com.core.ohmybills.security.UserContext;
import br.com.core.ohmybills.service.CreditCardServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/credit-cards")
public class CreditCardController {

    private final CreditCardServiceImpl creditCardService;

    public CreditCardController(CreditCardServiceImpl creditCardService) {
        this.creditCardService = creditCardService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public PageResponseDTO<CreditCardDTO> list(@CurrentUser UserContext user,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        return creditCardService.listCreditCards(user.userId(), page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public CreditCardDTO getById(@CurrentUser UserContext user, @PathVariable UUID id) {
        return creditCardService.findCreditCardById(user.userId(), id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Void> create(@CurrentUser UserContext user, @RequestBody @Valid CreditCardDTO creditCardDTO) {
        creditCardService.addCreditCard(user.userId(), creditCardDTO);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public CreditCardDTO update(@CurrentUser UserContext user, @RequestBody @Valid CreditCardDTO creditCardDTO) {
        return creditCardService.updateCreditCard(user.userId(), creditCardDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Void> delete(@CurrentUser UserContext user, @PathVariable UUID id) {
        creditCardService.deleteCreditCardById(user.userId(), id);
        return ResponseEntity.noContent().build();
    }
}