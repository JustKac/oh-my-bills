package br.com.core.ohmybills.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.core.ohmybills.model.PersonTag;

public interface PersonTagRepository extends JpaRepository<PersonTag, UUID> {

}
