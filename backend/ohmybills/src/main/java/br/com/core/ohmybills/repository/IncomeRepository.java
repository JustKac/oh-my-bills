package br.com.core.ohmybills.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.core.ohmybills.model.Income;

public interface IncomeRepository extends JpaRepository<Income, UUID> {

}
