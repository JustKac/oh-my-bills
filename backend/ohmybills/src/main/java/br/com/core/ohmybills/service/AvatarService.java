package br.com.core.ohmybills.service;

import br.com.core.ohmybills.model.UserAvatar;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface AvatarService {
    void save(UUID id, MultipartFile file) throws IOException;
    UserAvatar get(UUID id);
    void delete(UUID id);
}
