---

# **DOCUMENTO TÉCNICO: OH MY BILLS 💸**  
**Versão 1.0**  

---

## **1. Escopo do Projeto**  
### **Objetivo**  
Desenvolver um sistema de controle financeiro pessoal **offline** para pessoas físicas, focado em:  
- Rastreamento de receitas/despesas (incluindo parcelamentos).  
- Gestão de boletos, cartões de crédito e empréstimos.  
- Planejamento de metas e poupanças personalizadas.  

### **Público-Alvo**  
- Indivíduos com dificuldade de organizar despesas mensais, especialmente aqueles com múltiplas parcelas de cartão de crédito e contas recorrentes (água, luz, etc.).  

### **Plataformas**  
- **Frontend**: Aplicativo mobile (Flutter) e interface web.  
- **Backend**: API em Java/Quarkus (para sincronização futura, se necessário).  
- **Banco de Dados**: SQLite (armazenamento local no dispositivo).  

---

## **2. Requisitos Funcionais**  
### **Funcionalidades Principais**  
| Categoria               | Descrição                                                                                   |  
|-------------------------|---------------------------------------------------------------------------------------------|  
| **Controle Financeiro** | - Registro manual de receitas/despesas (recorrentes ou parceladas).<br>- Categorização por tipo (ex: "Alimentação", "Transporte").<br>- Orçamento mensal/anual com alertas de excedentes.<br>- Relatórios gráficos (barras, pizza) filtrados por período/categoria. |  
| **Boletos**             | - Cadastro manual de boletos (vencimento, valor, descrição).<br>- Alertas de vencimento via notificação. |  
| **Cartões de Crédito**  | - Cadastro de múltiplos cartões (bandeira, limite, dia de fechamento/vencimento).<br>- Acompanhamento de faturas (valor total, parcelas pendentes). |  
| **Empréstimos**         | - Cadastro de empréstimos (valor total, número de parcelas, taxa de juros).<br>- Acompanhamento de parcelas restantes. |  
| **Poupanças**           | - Criação de "poupanças" personalizadas (ex: "Viagem", "Reforma").<br>- Atribuição de valores a cada poupança.<br>- Vinculação de despesas específicas a uma poupança. |  

---

## **3. Requisitos Não Funcionais**  
| Categoria               | Descrição                                                                                   |  
|-------------------------|---------------------------------------------------------------------------------------------|  
| **Desempenho**          | - Tempo de resposta máximo de 2 segundos para operações críticas (ex: registro de transações). |  
| **Segurança**           | - Senha obrigatória para acesso ao app.<br>- Dados armazenados apenas localmente (SQLite). |  
| **Usabilidade**         | - Interface minimalista e intuitiva.<br>- Tutorial de onboarding em 3 passos. |  
| **Conformidade**        | - Adequação à LGPD (dados não compartilhados/coletados externamente). |  

---

## **4. Arquitetura Recomendada**  
### **Diagrama Simplificado**  
```  
[Frontend (Flutter)] ↔ [Backend Quarkus (Java)] ↔ [SQLite (Local)]  
```  

### **Detalhes**  
1. **Arquitetura Monolítica**: Recomendada para projetos de escopo pessoal e custo zero.  
   - **Vantagens**: Simplicidade, fácil manutenção, baixa complexidade.  
2. **Frontend**:  
   - **Flutter**: Permite desenvolvimento multiplataforma (iOS, Android, Web) com código único.  
3. **Backend**:  
   - **Quarkus**: Framework leve para APIs REST (opcional, caso deseje sincronização entre dispositivos futuramente).  
4. **Banco de Dados**:  
   - **SQLite**: Armazenamento local sem necessidade de servidor externo.  

---

## **5. Checklist de Conformidade Legal**  
- [x] Dados armazenados apenas no dispositivo do usuário (LGPD).  
- [x] Nenhum compartilhamento de dados com terceiros.  
- [x] Senha de acesso para proteger informações locais.  

---

## **6. Plano de Desenvolvimento**  
### **Fases Prioritárias**  
1. **MVP (Versão 1.0)**:  
   - Controle financeiro básico (registro de receitas/despesas, categorização).  
   - Gestão de cartões de crédito e boletos.  
   - Interface minimalista + onboarding.  
2. **Versão 2.0**:  
   - Funcionalidade de poupanças personalizadas.  
   - Relatórios gráficos.  
3. **Versão 3.0**:  
   - Feedback de usuários para ajustes (ex: melhorias em UX).  

### **Cronograma Sugerido**  
| Fase       | Duração Estimada |  
|------------|-------------------|  
| MVP        | 2-3 meses         |  
| Versão 2.0 | 1 mês             |  
| Versão 3.0 | 1 mês             |  

---

## **7. Recomendações Técnicas**  
1. **Flutter**: Use o pacote `sqflite` para integração com SQLite no mobile.  
2. **Quarkus**: Caso queira escalar para uma versão web, utilize Quarkus para criar uma API REST simples.  
3. **Testes**: Priorize testes de usabilidade com usuários reais para validar a interface.  

---

## **8. Riscos e Mitigação**  
| Risco                          | Mitigação                                      |  
|--------------------------------|------------------------------------------------|  
| Complexidade no parcelamento   | Implementar lógica simples de divisão de valores em "n" parcelas. |  
| Vazamento de dados locais      | Criptografar o banco SQLite com senha do usuário. |  

---

## **9. Documentação Futura**  
- Manual do usuário (PDF integrado ao app).  
- Changelog público para atualizações.  

---

Esse documento cobre todos os aspectos técnicos e funcionais do projeto. Se precisar de ajustes ou mais detalhes em alguma seção, avise! 😊