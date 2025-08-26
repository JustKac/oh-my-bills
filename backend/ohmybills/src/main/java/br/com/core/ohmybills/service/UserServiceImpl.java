package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.UserDTO;
import br.com.core.ohmybills.model.User;
import br.com.core.ohmybills.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl extends GenericServiceImpl<User, UUID, UserRepository> implements UserService {

    public UserServiceImpl(UserRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public UserDTO getMe(UUID userId, UUID keycloakId) {
        User userFound = repository.findByIdAndKeycloakId(userId, keycloakId).orElseThrow(EntityNotFoundException::new);
        return toDTO(userFound);
    }

    @Override
    public UUID resolveOrCreateUserIdBySub(UUID keycloakId, String email, String name) {

        Optional<User> existingByKc = repository.findByKeycloakId(keycloakId);
        if (existingByKc.isPresent()) {
            return existingByKc.get().getId();
        }

        if (email != null && !email.isBlank()) {
            Optional<User> existingByEmail = repository.findByEmail(email);
            if (existingByEmail.isPresent()) {
                User u = existingByEmail.get();
                if (u.getKeycloakId() == null) {
                    u.setKeycloakId(keycloakId);
                }
                if ((u.getName() == null || u.getName().isBlank()) && name != null) {
                    u.setName(name);
                }
                return repository.save(u).getId();
            }
        }

        User user = new User();
        user.setKeycloakId(keycloakId);
        user.setEmail(email);
        user.setName(name);
        return repository.save(user).getId();
    }

    private UserDTO toDTO(User u) {
        return new UserDTO(u.getId(), u.getName(), u.getEmail(), u.getKeycloakId());
    }
}