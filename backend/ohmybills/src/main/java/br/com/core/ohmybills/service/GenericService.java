package br.com.core.ohmybills.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenericService<T, ID> {

    T save(T entity);

    T findById(ID id);

    void deleteById(ID id);

    Page<T> findAll(Pageable pageable);

    boolean existsById(ID id);

}
