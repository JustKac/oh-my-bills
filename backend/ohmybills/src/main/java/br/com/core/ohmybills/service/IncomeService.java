package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.IncomeDTO;
import br.com.core.ohmybills.dto.PageResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IncomeService {

    IncomeDTO findIncomeById(UUID incomeId);
    void addIncome(IncomeDTO incomeDTO);
    IncomeDTO updateIncome(IncomeDTO incomeDTO);
    void deleteIncomeById(UUID incomeId);
    PageResponseDTO<IncomeDTO> listIncomes(int page, int size);
    void importIncomes(List<IncomeDTO> incomes);

}
