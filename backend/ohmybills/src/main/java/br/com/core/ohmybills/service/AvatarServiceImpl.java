package br.com.core.ohmybills.service;

import br.com.core.ohmybills.model.UserAvatar;
import br.com.core.ohmybills.repository.UserAvatarRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Service
public class AvatarServiceImpl extends GenericServiceImpl<UserAvatar, UUID, UserAvatarRepository> implements AvatarService {

    private static final long MAX_SIZE = 2L * 1024 * 1024; // 2 MB
    private static final Set<String> ALLOWED = Set.of("image/jpeg", "image/png", "image/webp");

    public AvatarServiceImpl(UserAvatarRepository repository) {
        super(repository);
    }

    @Override
    public void save(UUID id, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo não enviado.");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("Arquivo maior que 2MB.");
        }
        String ct = file.getContentType();
        if (ct == null || !ALLOWED.contains(ct.toLowerCase())) {
            throw new IllegalArgumentException("Tipo de arquivo não suportado. Use JPEG, PNG ou WEBP.");
        }

        UserAvatar avatar = findOrNew(id);
        avatar.setContentType(ct);
        avatar.setData(file.getBytes());
        save(avatar);
    }

    @Override
    public UserAvatar get(UUID id) {
        return findById(id);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    private UserAvatar findOrNew(UUID id) {
        try {
            return findById(id);
        } catch (EntityNotFoundException e) {
            UserAvatar avatar = new UserAvatar();
            avatar.setUserId(id);
            return avatar;
        }
    }
}