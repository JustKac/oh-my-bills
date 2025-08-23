# Etapa de build
FROM quay.io/keycloak/keycloak:26.2.4 AS builder

# Configurações de saúde, métricas e HTTP
ENV KC_HEALTH_ENABLED=true \
    KC_METRICS_ENABLED=true \
    KC_DB=postgres \
    KC_HOSTNAME_STRICT=false \
    KC_PROXY_HEADERS=forwarded \
    KC_HTTP_ENABLED=true \
    KC_HTTP_PORT=8080

WORKDIR /opt/keycloak

# Gera um keystore para HTTPS (não necessário se usar apenas HTTP)
RUN keytool -genkeypair \
    -storepass password \
    -storetype PKCS12 \
    -keyalg RSA \
    -keysize 2048 \
    -dname "CN=server" \
    -alias server \
    -ext "SAN=DNS:localhost,IP:127.0.0.1" \
    -keystore conf/server.keystore

# Add Theme
ADD --chown=keycloak:keycloak oh-my-bills-theme/target/oh-my-bills-theme-1.0.0.jar /opt/keycloak/providers/ohmybills-theme.jar
RUN /opt/keycloak/bin/kc.sh build

# Etapa final
FROM quay.io/keycloak/keycloak:26.2.4

COPY --from=builder /opt/keycloak/ /opt/keycloak/

ENV KC_HEALTH_ENABLED=true \
    KC_METRICS_ENABLED=true \
    KC_DB=postgres \
    KC_HOSTNAME_STRICT=false \
    KC_PROXY_HEADERS=forwarded \
    KC_HTTP_ENABLED=true \
    KC_HTTP_PORT=8080

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start", "--verbose", "--optimized"]