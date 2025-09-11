package br.com.core.ohmybills.service.impl;

import java.util.List;
import java.util.UUID;

import br.com.core.ohmybills.dto.PageResponseDTO;
import br.com.core.ohmybills.dto.TagDTO;
import br.com.core.ohmybills.service.TagService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.core.ohmybills.model.Tag;
import br.com.core.ohmybills.repository.TagRepository;

@Service
public class TagServiceImpl extends GenericServiceImpl<Tag, UUID, TagRepository> implements TagService {

    private final UserServiceImpl userService;

    public TagServiceImpl(TagRepository repository, UserServiceImpl userService) {
        super(repository);
        this.userService = userService;
    }

    @Override
    public PageResponseDTO<TagDTO> listTags(UUID userId, int page, int size) {
        var result = repository.findAllByUserId(userId, PageRequest.of(page, size));
        return new PageResponseDTO<>(
                result.getContent().stream().map(tag -> new TagDTO(
                        tag.getId(),
                        tag.getName(),
                        tag.isPerson(),
                        tag.getColor()
                )).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast()
        );
    }

    @Override
    public TagDTO findTagById(UUID userId, UUID id) {
        Tag tagFound = findByIdAndUserId(id, userId);
        return new TagDTO(
                tagFound.getId(),
                tagFound.getName(),
                tagFound.isPerson(),
                tagFound.getColor()
        );
    }

    @Override
    public void addTag(UUID userId, TagDTO tagDTO) {
        save(new Tag()
                .setName(tagDTO.name())
                .setIsPerson(tagDTO.isPerson())
                .setColor(tagDTO.color())
                .setUser(userService.findById(userId)));
    }

    @Override
    public TagDTO updateTag(UUID userId, TagDTO tagDTO) {
        Tag tagToUpdate = findByIdAndUserId(tagDTO.tagId(), userId);
        tagToUpdate.setName(tagDTO.name())
                .setIsPerson(tagDTO.isPerson())
                .setColor(tagDTO.color());

        save(tagToUpdate);
        return new TagDTO(
                tagToUpdate.getId(),
                tagToUpdate.getName(),
                tagToUpdate.isPerson(),
                tagToUpdate.getColor()
        );
    }

    @Override
    public void deleteTagById(UUID userId, UUID id) {
        findByIdAndUserId(id, userId);
        deleteById(id);
    }

    @Override
    public List<TagDTO> findByExpenseId(UUID userId, UUID expenseId) {
        return repository.findByUserIdAndExpensesId(userId, expenseId).stream().map(tag -> new TagDTO(
                tag.getId(),
                tag.getName(),
                tag.isPerson(),
                tag.getColor()
        )).toList();
    }

    protected Tag findByIdAndUserId(UUID id, UUID userId) {
        return repository.findByIdAndUserId(id, userId).orElseThrow(EntityNotFoundException::new);
    }
}
