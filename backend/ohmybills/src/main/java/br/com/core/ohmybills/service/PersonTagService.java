package br.com.core.ohmybills.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.core.ohmybills.model.PersonTag;
import br.com.core.ohmybills.repository.PersonTagRepository;

@Service
public class PersonTagService extends GenericServiceImpl<PersonTag, UUID, PersonTagRepository> {

    public PersonTagService(PersonTagRepository repository) {
        super(repository);
    }

}
