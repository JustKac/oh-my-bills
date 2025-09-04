package br.com.core.ohmybills.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.core.ohmybills.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    Optional<Expense> findByIdAndUserId(UUID id, UUID userId);
    Page<Expense> findAllByUserId(UUID userId, Pageable pageable);

    // Requests for MainPage
    List<Expense> findByUser_IdAndFirstPayDateBeforeAndIsArchivedFalse(UUID userId, LocalDate firstPayDateBefore);
    List<Expense> findByUser_IdAndIsRecurringTrueAndIsArchivedFalse(UUID userId);
}
