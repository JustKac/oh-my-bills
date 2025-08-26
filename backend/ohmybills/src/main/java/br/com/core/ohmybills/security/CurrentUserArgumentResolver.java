package br.com.core.ohmybills.security;

import br.com.core.ohmybills.service.UserService;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;
import java.util.UUID;

@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;

    public CurrentUserArgumentResolver(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && parameter.getParameterType().equals(UserContext.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;

        String sub;
        String email = null;
        String name = null;

        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            var jwt = jwtAuth.getToken();
            sub = jwt.getSubject();
            email = jwt.getClaimAsString("email");
            name = firstNonBlank(jwt.getClaimAsString("name"), jwt.getClaimAsString("preferred_username"));
        } else if (auth.getPrincipal() instanceof OidcUser oidc) {
            Map<String, Object> claims = oidc.getClaims();
            sub = oidc.getSubject();
            email = (String) claims.get("email");
            name = firstNonBlank((String) claims.get("name"), (String) claims.get("preferred_username"));
        } else {
            throw new IllegalStateException("Tipo de autenticação não suportado.");
        }

        UUID keycloakId = parseUuid(sub);
        UUID userId = userService.resolveOrCreateUserIdBySub(keycloakId, email, name);
        return new UserContext(userId, keycloakId);
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
            throw new IllegalStateException("Keycloak 'sub' não é um UUID válido: " + value);
        }
    }
}