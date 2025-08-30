package br.com.core.ohmybills.service;

import br.com.core.ohmybills.dto.AuthTokenDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

public interface AuthService {

    ResponseEntity<Void> buildLoginRedirect();

    ResponseEntity<Void> buildLogoutRedirect();

    AuthTokenDTO getCurrentToken(Authentication auth, OAuth2AuthorizedClient client);
}