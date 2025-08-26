package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.IncomeDTO;
import br.com.core.ohmybills.dto.PageResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IncomeService {
    PageResponseDTO<IncomeDTO> listIncomes(UUID userId, int page, int size);
    IncomeDTO findIncomeById(UUID userId, UUID id);
    void addIncome(UUID userId, IncomeDTO incomeDTO);
    IncomeDTO updateIncome(UUID userId, IncomeDTO incomeDTO);
    void deleteIncomeById(UUID userId, UUID id);
    void importIncomes(UUID userId, List<IncomeDTO> incomes);
}