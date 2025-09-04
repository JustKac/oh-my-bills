package br.com.core.ohmybills.controller;

import br.com.core.ohmybills.dto.TagDTO;
import br.com.core.ohmybills.dto.PageResponseDTO;
import br.com.core.ohmybills.security.CurrentUser;
import br.com.core.ohmybills.security.UserContext;
import br.com.core.ohmybills.service.TagServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagServiceImpl tagService;

    public TagController(TagServiceImpl tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public PageResponseDTO<TagDTO> list(@CurrentUser UserContext user,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        return tagService.listTags(user.userId(), page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public TagDTO getById(@CurrentUser UserContext user, @PathVariable UUID id) {
        return tagService.findTagById(user.userId(), id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Void> create(@CurrentUser UserContext user, @RequestBody @Valid TagDTO tagDTO) {
        tagService.addTag(user.userId(), tagDTO);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public TagDTO update(@CurrentUser UserContext user, @RequestBody @Valid TagDTO tagDTO) {
        return tagService.updateTag(user.userId(), tagDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Void> delete(@CurrentUser UserContext user, @PathVariable UUID id) {
        tagService.deleteTagById(user.userId(), id);
        return ResponseEntity.noContent().build();
    }
}