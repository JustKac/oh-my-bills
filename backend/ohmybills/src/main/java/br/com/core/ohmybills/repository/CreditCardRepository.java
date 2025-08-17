package br.com.core.ohmybills.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.core.ohmybills.model.CreditCard;

public interface CreditCardRepository extends JpaRepository<CreditCard, UUID> {

}
