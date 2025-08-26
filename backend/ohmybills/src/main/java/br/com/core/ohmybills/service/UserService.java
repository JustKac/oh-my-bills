package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.UserDTO;

import java.util.UUID;

public interface UserService {
    UserDTO getMe(UUID userId, UUID keycloakId);
    UUID resolveOrCreateUserIdBySub(UUID keycloakId, String email, String name);
}
