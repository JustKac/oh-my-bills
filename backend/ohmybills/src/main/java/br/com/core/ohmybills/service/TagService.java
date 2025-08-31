package br.com.core.ohmybills.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.core.ohmybills.model.Tag;
import br.com.core.ohmybills.repository.TagRepository;

@Service
public class TagService extends GenericServiceImpl<Tag, UUID, TagRepository> {

    public TagService(TagRepository repository) {
        super(repository);
    }

}
