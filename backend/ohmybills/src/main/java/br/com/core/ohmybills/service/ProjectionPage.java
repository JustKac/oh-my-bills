package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.ProjectionDTO;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public interface ProjectionPage {
    /**
     * Obtém a projeção financeira para os próximos meses
     *
     * @param userId     ID do usuário
     * @param startMonth Mês inicial da projeção
     * @param months     Número de meses a projetar (máx 12)
     * @return Lista de projeções mensais
     */
    List<ProjectionDTO> getFinancialProjection(UUID userId, YearMonth startMonth, int months);

    /**
     * Obtém a projeção financeira filtrada por tags
     *
     * @param userId     ID do usuário
     * @param startMonth Mês inicial da projeção
     * @param months     Número de meses a projetar
     * @param tagIds     IDs das tags para filtrar
     * @return Lista de projeções mensais filtradas
     */
    List<ProjectionDTO> getProjectionFilteredByTags(UUID userId, YearMonth startMonth, int months, List<UUID> tagIds);

    /**
     * Obtém a projeção financeira filtrada por cartões de crédito
     *
     * @param userId     ID do usuário
     * @param startMonth Mês inicial da projeção
     * @param months     Número de meses a projetar
     * @param cardIds    IDs dos cartões para filtrar
     * @return Lista de projeções mensais filtradas
     */
    List<ProjectionDTO> getProjectionFilteredByCards(UUID userId, YearMonth startMonth, int months, List<UUID> cardIds);

    /**
     * Obtém a projeção financeira filtrada por tags e cartões de crédito
     *
     * @param userId     ID do usuário
     * @param startMonth Mês inicial da projeção
     * @param months     Número de meses a projetar
     * @param tagIds     IDs das tags para filtrar
     * @param cardIds    IDs dos cartões para filtrar
     * @return Lista de projeções mensais filtradas
     */
    List<ProjectionDTO> getProjectionFilteredByTagsAndCards(
            UUID userId, YearMonth startMonth, int months, List<UUID> tagIds, List<UUID> cardIds);
}