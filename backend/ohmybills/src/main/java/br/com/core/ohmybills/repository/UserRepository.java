package br.com.core.ohmybills.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.core.ohmybills.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {

}
