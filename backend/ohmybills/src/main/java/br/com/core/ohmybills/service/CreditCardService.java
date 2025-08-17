package br.com.core.ohmybills.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.core.ohmybills.model.CreditCard;
import br.com.core.ohmybills.repository.CreditCardRepository;

@Service
public class CreditCardService extends GenericServiceImpl<CreditCard, UUID, CreditCardRepository> {

    public CreditCardService(CreditCardRepository repository) {
        super(repository);
    }

}
