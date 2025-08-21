package br.com.core.ohmybills.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.EntityNotFoundException;

public abstract class GenericServiceImpl<T, ID, R extends JpaRepository<T, ID>> implements GenericService<T, ID> {

    protected final R repository;

    protected GenericServiceImpl(R repository) {
        this.repository = repository;
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public T findById(ID id) {
        return repository.findById(id).orElseThrow((EntityNotFoundException::new));
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

}
