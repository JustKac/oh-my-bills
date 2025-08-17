# =========================================================================
# BUILD STAGE
# Define a imagem de build com Maven e Java 21
# =========================================================================
FROM maven:3.9-eclipse-temurin-21 AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia o pom.xml para o cache de dependências do Docker
# Isso evita o download de dependências a cada alteração no código-fonte
COPY pom.xml .

# Baixa todas as dependências do projeto
RUN mvn dependency:go-offline

# Copia o restante do código-fonte da aplicação
COPY src ./src

# Compila a aplicação e gera o JAR, pulando os testes
# Usamos 'package' que é suficiente para criar o JAR executável
RUN mvn clean package -DskipTests

# =========================================================================
# RUNTIME STAGE
# Define a imagem final, otimizada e segura para execução
# =========================================================================
FROM eclipse-temurin:21-jre-jammy

# Define o diretório de trabalho
WORKDIR /app

# Argumento para o ID do usuário, para flexibilidade
ARG UID=1001

# Cria um usuário e grupo específicos para a aplicação, sem shell e sem diretório home
# Esta é uma prática de segurança crucial para evitar rodar como root
RUN addgroup --system spring && \
    adduser --system --uid ${UID} --ingroup spring springuser

# Copia o JAR compilado do estágio de 'build'
# O wildcard (*) garante que pegue o JAR mesmo que a versão mude
# Renomear para 'app.jar' simplifica o comando ENTRYPOINT
COPY --from=build /app/target/*.jar app.jar

# Define o proprietário do arquivo JAR para o usuário criado
RUN chown springuser:spring app.jar

# Muda para o usuário não-root
USER springuser

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
# O formato 'exec' (com colchetes) é preferível pois o processo da JVM se torna o PID 1,
# o que permite que ele receba sinais do Docker corretamente (ex: para parar o container)
ENTRYPOINT ["java", "-jar", "app.jar"]