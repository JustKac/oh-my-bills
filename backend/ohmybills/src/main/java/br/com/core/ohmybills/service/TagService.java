package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.PageResponseDTO;
import br.com.core.ohmybills.dto.TagDTO;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public interface TagService {

    PageResponseDTO<TagDTO> listTags(UUID userId, int page, int size);
    TagDTO findTagById(UUID userId, UUID id);
    void addTag(UUID userId, TagDTO tagDTO);
    TagDTO updateTag(UUID userId, TagDTO tagDTO);
    void deleteTagById(UUID userId, UUID id);
    List<TagDTO> findByExpenseId(UUID userId, UUID expenseId);

}
