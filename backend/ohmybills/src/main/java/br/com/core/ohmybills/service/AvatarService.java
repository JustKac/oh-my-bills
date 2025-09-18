package br.com.core.ohmybills.service;

import br.com.core.ohmybills.model.UserAvatar;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@SuppressWarnings("unused")
public interface AvatarService {
    void save(UUID id, MultipartFile file);
    UserAvatar get(UUID id);
    void delete(UUID id);
}
