package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.UserDTO;
import br.com.core.ohmybills.model.User;
import br.com.core.ohmybills.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl extends GenericServiceImpl<User, UUID, UserRepository> implements UserService {

    public UserServiceImpl(UserRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public UserDTO getMe(Authentication auth, OAuth2AuthorizedClient client) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("Requisição não autenticada.");
        }

        // Extrai claims do JWT ou do principal OIDC
        String sub;
        String email;
        String name;

        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            var jwt = jwtAuth.getToken();
            sub = jwt.getSubject();
            email = jwt.getClaimAsString("email");
            name = firstNonBlank(jwt.getClaimAsString("name"), jwt.getClaimAsString("preferred_username"));
        } else {
            var principal = auth.getPrincipal();
            if (principal instanceof org.springframework.security.oauth2.core.oidc.user.OidcUser oidcUser) {
                Map<String, Object> claims = oidcUser.getClaims();
                sub = oidcUser.getSubject();
                email = (String) claims.get("email");
                name = firstNonBlank((String) claims.get("name"), (String) claims.get("preferred_username"));
            } else {
                throw new IllegalStateException("Tipo de autenticação não suportado para resolver Keycloak ID.");
            }
        }

        if (sub == null || sub.isBlank()) {
            throw new IllegalStateException("Keycloak 'sub' ausente no token.");
        }
        UUID keycloakId = parseUuid(sub);

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
                // Se ainda não estava vinculado, vincula agora
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

    private String firstNonBlank(String a, String b) {
        if (a != null && !a.isBlank()) return a;
        if (b != null && !b.isBlank()) return b;
        return null;
    }

    private UUID parseUuid(String value) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("O 'sub' do Keycloak não é um UUID válido: " + value);
        }
    }
}