package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.AuthPrincipalDTO;
import br.com.core.ohmybills.dto.UserDTO;
import br.com.core.ohmybills.model.User;
import br.com.core.ohmybills.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl extends GenericServiceImpl<User, UUID, UserRepository> implements UserService {

    private final AuthService authService;

    public UserServiceImpl(UserRepository repository, AuthService authService) {
        super(repository);
        this.authService = authService;
    }

    @Override
    @Transactional
    public UserDTO getMe(Authentication auth, OAuth2AuthorizedClient client) {
        AuthPrincipalDTO principal = authService.resolvePrincipal(auth, client);

        UUID keycloakId = principal.keycloakId();
        String email = principal.email();
        String name = principal.name();

        // 1) Busca por keycloakId
        Optional<User> existingByKc = repository.findByKeycloakId(keycloakId);
        if (existingByKc.isPresent()) {
            return toDTO(existingByKc.get());
        }

        // 2) Se não achou, tenta por e‑mail para evitar violar a unicidade
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
                return toDTO(repository.save(u));
            }
        }

        // 3) Cria novo usuário
        User user = new User();
        user.setKeycloakId(keycloakId);
        user.setEmail(email);
        user.setName(name);
        User saved = repository.save(user);

        return toDTO(saved);
    }

    private UserDTO toDTO(User u) {
        return new UserDTO(u.getId(), u.getName(), u.getEmail(), u.getKeycloakId());
    }
}