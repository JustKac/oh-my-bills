package br.com.core.ohmybills.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.core.ohmybills.model.Income;

public interface IncomeRepository extends JpaRepository<Income, UUID> {
    Optional<Income> findByIdAndUserId(UUID id, UUID userId);
    Page<Income> findAllByUserId(UUID userId, Pageable pageable);
}
