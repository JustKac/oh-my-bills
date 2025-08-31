package br.com.core.ohmybills.service;

import java.util.List;
import java.util.UUID;

import br.com.core.ohmybills.dto.CreditCardDTO;
import br.com.core.ohmybills.dto.PageResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import br.com.core.ohmybills.model.CreditCard;
import br.com.core.ohmybills.repository.CreditCardRepository;

@Service
public class CreditCardServiceImpl extends GenericServiceImpl<CreditCard, UUID, CreditCardRepository> implements CreditCardService {

    private final UserServiceImpl userService;

    public CreditCardServiceImpl(CreditCardRepository repository, UserServiceImpl userService) {
        super(repository);
        this.userService = userService;
    }

    @Override
    public PageResponseDTO<CreditCardDTO> listCreditCards(UUID userId, int page, int size) {
        var result = repository.findAllByUserId(userId, org.springframework.data.domain.PageRequest.of(page, size));
        return new PageResponseDTO<>(
                result.getContent().stream().map(creditCard -> new CreditCardDTO(
                        creditCard.getId(),
                        creditCard.getName(),
                        creditCard.getLastFourDigits(),
                        creditCard.getBrand(),
                        creditCard.getCreditLimit(),
                        creditCard.getDueDate(),
                        creditCard.getBestShoppingDay()
                )).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast()
        );
    }

    @Override
    public CreditCardDTO findCreditCardById(UUID userId, UUID id) {
        CreditCard creditCardFound = findByIdAndUserId(id, userId);
        return new CreditCardDTO(
                creditCardFound.getId(),
                creditCardFound.getName(),
                creditCardFound.getLastFourDigits(),
                creditCardFound.getBrand(),
                creditCardFound.getCreditLimit(),
                creditCardFound.getDueDate(),
                creditCardFound.getBestShoppingDay()
        );
    }

    @Override
    public void addCreditCard(UUID userId, CreditCardDTO creditCardDTO) {
        save(new CreditCard()
                .setName(creditCardDTO.name())
                .setLastFourDigits(creditCardDTO.lastFourDigits())
                .setBrand(creditCardDTO.brand())
                .setCreditLimit(creditCardDTO.creditLimit())
                .setDueDate(creditCardDTO.dueDate())
                .setBestShoppingDay(creditCardDTO.bestShoppingDay())
                .setUser(userService.findById(userId)));
    }

    @Override
    public CreditCardDTO updateCreditCard(UUID userId, CreditCardDTO creditCardDTO) {
        CreditCard creditCardToUpdate = findByIdAndUserId(creditCardDTO.creditCardId(), userId);
        creditCardToUpdate
                .setName(creditCardDTO.name())
                .setCreditLimit(creditCardDTO.creditLimit())
                .setDueDate(creditCardDTO.dueDate())
                .setBestShoppingDay(creditCardDTO.bestShoppingDay());
        return new CreditCardDTO(
                save(creditCardToUpdate).getId(),
                creditCardToUpdate.getName(),
                creditCardToUpdate.getLastFourDigits(),
                creditCardToUpdate.getBrand(),
                creditCardToUpdate.getCreditLimit(),
                creditCardToUpdate.getDueDate(),
                creditCardToUpdate.getBestShoppingDay()
        );
    }

    @Override
    public void deleteCreditCardById(UUID userId, UUID id) {
        findByIdAndUserId(id, userId);
        deleteById(id);
    }

    protected CreditCard findByIdAndUserId(UUID id, UUID userId) {
        return repository.findByIdAndUserId(id, userId).orElseThrow(EntityNotFoundException::new);
    }
}
