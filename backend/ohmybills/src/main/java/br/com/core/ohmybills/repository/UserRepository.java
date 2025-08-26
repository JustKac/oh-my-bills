package br.com.core.ohmybills.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.core.ohmybills.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByKeycloakId(UUID keycloakId);
    Optional<User> findByEmail(String email);
    Optional<User> findByIdAndKeycloakId(UUID id, UUID keycloakId);
}
