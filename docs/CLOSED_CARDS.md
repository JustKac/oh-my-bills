Cards concluídos

- App base Spring Boot
  - `OhmybillsApplication.java` e `ServletInitializer.java` criados e funcionando.

- Modelo de domínio e camada de persistência
  - Entidades: `User`, `Income`, `Expense`, `CreditCard`, `Invoice`, `PersonTag`, `AbstractEntity`, `AuditableEntity`.
  - Repositórios JPA: `UserRepository`, `IncomeRepository`, `ExpenseRepository`, `CreditCardRepository`, `InvoiceRepository`, `PersonTagRepository`.

- DTOs da aplicação
  - `UserDTO`, `IncomeDTO`, `ExpenseDTO`, `PageResponseDTO`, `AuthTokenDTO`, `AuthPrincipalDTO`.

- Segurança e integração com Keycloak
  - `SecurityConfig` com OAuth2 Login, Resource Server JWT, `@EnableMethodSecurity`, autorização por rotas, CORS, CSRF e H2 liberado.
  - Conversor de roles `KeycloakAuthoritiesConverter` mapeando roles do Keycloak para `ROLE_USER` e `ROLE_ADMIN`.
  - Logout OIDC com `OidcClientInitiatedLogoutSuccessHandler` e `postLogoutRedirectUri` usando `{baseUrl}`.
  - `WebConfig` com `ForwardedHeaderFilter` para respeitar `X‑Forwarded‑*`.

- Autenticação e perfil
  - `AuthController` com endpoints: `/auth/login`, `/auth/me`, `/auth/token`, `/auth/logout`.
  - `AuthService` e `AuthServiceImpl`:
    - Redirecionamento para iniciar login OIDC.
    - Redirecionamento de logout.
    - Suporte a sessão OIDC e Bearer JWT para expor o token atual e principal do usuário.

- Regras de autorização por role
  - `@PreAuthorize` aplicado nos controllers conforme `ROLE_USER` e `ROLE_ADMIN`.

- Incomes API
  - `IncomeController` implementado:
    - `GET /incomes` paginação via `PageResponseDTO`.
    - `GET /incomes/{id}` detalhe.
    - `POST /incomes` criação.
    - `PUT /incomes/{id}` atualização.
    - `DELETE /incomes/{id}` exclusão.
    - `POST /incomes/import` importação em lote.
    - Autorização: leitura `USER` ou `ADMIN`; escrita apenas `ADMIN`.

- Serviços de domínio
  - Interfaces: `IncomeService`, `ExpenseService`, `CreditCardService`, `InvoiceService`, `PersonTagService`, `UserService`, `GenericService`.
  - Implementações presentes: `IncomeServiceImpl`, `ExpenseServiceImpl`, `UserServiceImpl`, `GenericServiceImpl`.