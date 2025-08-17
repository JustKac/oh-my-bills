package br.com.core.ohmybills.service;

public interface GenericService<T, ID> {

    T save(T entity);

    T findById(ID id);

    void deleteById(ID id);

    Iterable<T> findAll();

    boolean existsById(ID id);

}
