package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.CreditCardDTO;
import br.com.core.ohmybills.dto.PageResponseDTO;

import java.util.UUID;

@SuppressWarnings("unused")
public interface CreditCardService {

    PageResponseDTO<CreditCardDTO> listCreditCards(UUID userId, int page, int size);
    CreditCardDTO findCreditCardById(UUID userId, UUID id);
    void addCreditCard(UUID userId, CreditCardDTO creditCardDTO);
    CreditCardDTO updateCreditCard(UUID userId, CreditCardDTO creditCardDTO);
    void deleteCreditCardById(UUID userId, UUID id);

}
