package br.com.core.ohmybills.controller;

import br.com.core.ohmybills.dto.ProjectionDTO;
import br.com.core.ohmybills.security.CurrentUser;
import br.com.core.ohmybills.security.UserContext;
import br.com.core.ohmybills.service.impl.ProjectionPageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projections")
public class ProjectionController {

    private final ProjectionPageImpl projectionService;

    public ProjectionController(ProjectionPageImpl projectionService) {
        this.projectionService = projectionService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ProjectionDTO>> getFinancialProjection(
            @CurrentUser UserContext user,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth startMonth,
            @RequestParam(defaultValue = "12") int months) {

        return ResponseEntity.ok(
                projectionService.getFinancialProjection(user.userId(), startMonth, months)
        );
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<ProjectionDTO>> getFilteredProjection(
            @CurrentUser UserContext user,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth startMonth,
            @RequestParam(defaultValue = "12") int months,
            @RequestParam(required = false) List<UUID> tagIds,
            @RequestParam(required = false) List<UUID> cardIds) {

        // Verifica quais filtros foram fornecidos
        boolean hasTags = tagIds != null && !tagIds.isEmpty();
        boolean hasCards = cardIds != null && !cardIds.isEmpty();

        return ResponseEntity.ok(getProjectionDTOS(
                user, startMonth, months,
                tagIds, cardIds, hasTags, hasCards));
    }

    private List<ProjectionDTO> getProjectionDTOS(UserContext user, YearMonth startMonth, int months, List<UUID> tagIds, List<UUID> cardIds, boolean hasTags, boolean hasCards) {
        List<ProjectionDTO> result;

        if (hasTags && hasCards) {
            // Filtrar por tags e cartões
            result = projectionService.getProjectionFilteredByTagsAndCards(
                    user.userId(), startMonth, months, tagIds, cardIds);
        } else if (hasTags) {
            // Filtrar apenas por tags
            result = projectionService.getProjectionFilteredByTags(
                    user.userId(), startMonth, months, tagIds);
        } else if (hasCards) {
            // Filtrar apenas por cartões
            result = projectionService.getProjectionFilteredByCards(
                    user.userId(), startMonth, months, cardIds);
        } else {
            // Sem filtros, retorna projeção completa
            result = projectionService.getFinancialProjection(
                    user.userId(), startMonth, months);
        }
        return result;
    }
}