package br.com.core.ohmybills.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.core.ohmybills.model.Tag;

public interface TagRepository extends JpaRepository<Tag, UUID> {
    Optional<Tag> findByIdAndUserId(UUID id, UUID userId);
    Page<Tag> findAllByUserId(UUID userId, Pageable pageable);
    List<Tag> findByUserIdAndExpensesId(UUID userId, UUID expenseId);
}
