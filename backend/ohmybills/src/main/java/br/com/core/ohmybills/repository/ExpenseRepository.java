package br.com.core.ohmybills.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.core.ohmybills.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.core.ohmybills.model.Expense;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    Optional<Expense> findByIdAndUserId(UUID id, UUID userId);
    Page<Expense> findAllByUserId(UUID userId, Pageable pageable);
}
