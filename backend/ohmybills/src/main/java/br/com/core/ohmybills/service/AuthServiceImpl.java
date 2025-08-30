package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.AuthTokenDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public ResponseEntity<Void> buildLoginRedirect() {
        String location = ServletUriComponentsBuilder
                .fromCurrentContextPath() // inclui /api
                .path("/oauth2/authorization/keycloak")
                .build()
                .toUriString();
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, location)
                .build();
    }

    @Override
    public ResponseEntity<Void> buildLogoutRedirect() {
        String location = ServletUriComponentsBuilder
                .fromCurrentContextPath() // inclui /api
                .path("/logout")
                .build()
                .toUriString();
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, location)
                .build();
    }

    @Override
    public AuthTokenDTO getCurrentToken(Authentication auth, OAuth2AuthorizedClient client) {
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            var jwt = jwtAuth.getToken();
            return new AuthTokenDTO(jwt.getTokenValue(), jwt.getExpiresAt());
        }
        if (client != null && client.getAccessToken() != null) {
            String accessToken = client.getAccessToken().getTokenValue();
            Instant accessTokenExpiresAt = client.getAccessToken().getExpiresAt();
            return new AuthTokenDTO(accessToken, accessTokenExpiresAt);
        }
        throw new IllegalStateException("Nenhum token disponível para o contexto atual.");
    }

}