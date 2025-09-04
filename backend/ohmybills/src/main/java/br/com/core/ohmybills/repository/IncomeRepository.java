package br.com.core.ohmybills.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.core.ohmybills.model.Income;

public interface IncomeRepository extends JpaRepository<Income, UUID> {
    Optional<Income> findByIdAndUserId(UUID id, UUID userId);
    Page<Income> findAllByUserId(UUID userId, Pageable pageable);

    List<Income> findByUser_IdAndFirstPayDateBefore(UUID userId, LocalDate firstPayDateBefore);


}
