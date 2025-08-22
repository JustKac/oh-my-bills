FROM quay.io/keycloak/keycloak:26.2.4 AS builder
# Enable health and metrics support
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

# Configure a database vendor
ENV KC_DB=postgres
ENV KC_HOSTNAME_STRICT=false
ENV KC_PROXY_HEADERS=forwarded
ENV KC_HTTP_ENABLED=true
ENV KC_HTTP_PORT=8080

WORKDIR /opt/keycloak
# for demonstration purposes only, please make sure to use proper certificates in production instead
RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname "CN=server" -alias server -ext "SAN=DNS:localhost,IP:127.0.0.1" -keystore conf/server.keystore

# Add Theme
ADD --chown=keycloak:keycloak oh-my-bills-theme/target/oh-my-bills-theme-1.0.0.jar /opt/keycloak/providers/ohmybills-theme.jar
RUN /opt/keycloak/bin/kc.sh build

# Usar a imagem oficial do Keycloak na versão 26.2.4
FROM quay.io/keycloak/keycloak:26.2.4
COPY --from=builder /opt/keycloak/ /opt/keycloak/

# change these values to point to a running postgres instance
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true
ENV KC_DB=postgres
ENV KC_HOSTNAME_STRICT=false
ENV KC_PROXY_HEADERS=forwarded
ENV KC_HTTP_ENABLED=true
ENV KC_HTTP_PORT=8080

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start", "--verbose" ,"--optimized" ]