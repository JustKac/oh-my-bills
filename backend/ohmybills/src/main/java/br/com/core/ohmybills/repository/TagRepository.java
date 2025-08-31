package br.com.core.ohmybills.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.core.ohmybills.model.Tag;

public interface TagRepository extends JpaRepository<Tag, UUID> {

}
