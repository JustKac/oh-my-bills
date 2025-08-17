# 📄 Regras de Negócio – Oh My Bills

## 1. Autenticação e Segurança
**Banco de dados:** PostgreSQL.  
**Autenticação:** Keycloak (Open Source, hospedado pelo próprio time).  

### 1.1 Configuração de Keycloak
- Uso de **OpenID Connect (OIDC)** para autenticação.
- Aplicação registrada como *client* no Keycloak.
- Tokens **JWT** emitidos pelo Keycloak.
- Sessões e refresh tokens gerenciados pelo Keycloak.

### 1.2 Cadastro
- Realizado via Keycloak (self-registration habilitado).
- E-mail deve ser único no sistema.
- Regras de senha definidas no Keycloak:
  - Mínimo 8 caracteres.
  - Pelo menos 1 letra, 1 número e 1 caractere especial.

### 1.3 Login
- Redirecionamento para página de login do Keycloak.
- Após autenticação, usuário é redirecionado para o sistema com token válido.
- Tokens validados em cada requisição via API.

### 1.4 Recuperação de senha
- Feita diretamente no Keycloak, via link enviado por e-mail.
- Link expira em 30 minutos.

### 1.5 Sessões
- Expiram em **30 minutos** de inatividade.
- Refresh token válido por **7 dias**.
- Logout centralizado no Keycloak encerra todas as sessões.

---

## 2. Dashboard
- Saldo considera rendas e despesas ativas do mês, incluindo recorrentes e parcelas.
- Cobranças pendentes aparecem em destaque.
- Carregamento ≤ **2 segundos**.

---

## 3. Rendas e Despesas
### 3.1 Campos obrigatórios
- Título, valor, data inicial(mês/ano), número de parcelas ou recorrência.

### 3.2 Parcelas
- Parcelas = 1 → ocorrência única.
- Parcelas > 1 → gerar registros futuros até a quantidade definida.
- Recorrente → gerar mensalmente sem data final.

### 3.3 Tags
- Criadas no momento do cadastro ou reutilizadas via menu suspenso com busca.
- Uma transação pode ter várias tags.

### 3.4 Edição
- Ao editar item parcelado, perguntar se a alteração afeta apenas a parcela atual ou todas as futuras.

### 3.5 Exclusão
- Exclusão lógica → item vai para “Arquivados”.
- Itens arquivados podem ser restaurados ou excluídos definitivamente.

---

## 4. Itens Arquivados
- Não aparecem na dashboard nem projeções.
- Possuem filtros, pesquisa e paginação.
- Exclusão definitiva remove do banco.

---

## 5. Projeção de 12 Meses
- Considera parcelas futuras, recorrências e previsões.
- Pode ser filtrada por categoria ou tags.
- Atualiza automaticamente com alterações nos lançamentos.

---

## 6. Cobranças
### 6.1 Criação
- Associar a pessoa e/ou fatura.
- Status inicial: "Pendente".
- Enviar e-mail no momento da criação.

### 6.2 Pagamentos
- Ao marcar como paga, registrar data.
- Alterar status para "Paga".
- Remover alerta programado.

### 6.3 Alertas
- E-mail automático enviado **3 dias antes** do fim do mês para pendentes.

---

## 7. Importação CSV (Wizard)
### 7.1 Fluxo
1. Upload do arquivo.
2. Mapeamento das colunas.
3. Validação dos dados.
4. Importação final.

### 7.2 Validação
- Mostrar amostra das linhas.
- Indicar erros e motivos.
- Permitir corrigir antes de importar.

### 7.3 Tags
- Criar novas durante importação.
- Sugerir existentes.

---

## 8. Perfil e Configurações
- Foto: máx. 2 MB, formatos JPG, PNG, WEBP.
- Exclusão de conta: botão envia requisição para Keycloak e remove dados no banco.
- Tema claro/escuro: salvar preferência, aplicar sem recarregar página.

---

## 9. UX e Design
- Layout minimalista, espaçamento consistente, tipografia legível.
- Cantos arredondados (8–12px), ícones claros.
- Efeitos de hover, animações suaves, feedback visual claro.
- Responsivo para desktop, tablet e mobile.
- Menu lateral retrátil em telas pequenas.

---

## 10. Performance
- Consultas otimizadas para PostgreSQL.
- Paginação para listas > 20 itens.
- Dashboard ≤ 2 segundos.

---

## 11. Segurança
- Toda comunicação via **HTTPS**.
- Prevenir injeção SQL com consultas parametrizadas.
- Proteção contra XSS e CSRF.
- Validação de tokens JWT emitidos pelo Keycloak.
