# 💸 OH MY BILLS

Um aplicativo de controle financeiro pessoal e **offline** para ajudar você a gerenciar suas receitas, despesas, boletos, cartões de crédito e metas de poupança de forma simples e eficiente.

---

## 🚀 Funcionalidades

- **Controle Financeiro Básico**:
  - Registro de receitas e despesas.
  - Categorização de transações.
  - Alertas de gastos próximos ao limite da renda.

- **Gestão de Boletos**:
  - Cadastro de boletos.
  - Alertas de vencimento.

- **Cartões de Crédito**:
  - Cadastro de múltiplos cartões.
  - Acompanhamento de faturas (datas de fechamento e vencimento).

- **Empréstimos**:
  - Cadastro e acompanhamento de parcelas.

- **Poupanças**:
  - Criação de metas personalizadas (ex: "Viagem", "Reforma").
  - Acompanhamento de depósitos e saques.

---

## 🛠️ Tecnologias Utilizadas

- **Frontend**: Flutter (mobile e web).
- **Backend**: Java com Quarkus (API REST).
- **Banco de Dados**: SQLite (armazenamento local).
- **Ferramentas**: Git, GitHub, Figma (para prototipagem).

---

## 📦 Estrutura do Projeto

```plaintext
oh-my-bills/
├── backend/              # Código do backend (Java/Quarkus)
├── frontend/             # Código do frontend (Flutter)
├── docs/                 # Documentação do projeto
└── README.md             # Este arquivo
```

---

## 🖥️ Como Executar o Projeto

### Pré-requisitos
- Flutter SDK instalado.
- Java JDK 17+ instalado.
- SQLite (embutido no Flutter ou no Quarkus).

### Passos para Executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/JustKac/oh-my-bills.git
   cd oh-my-bills
   ```

2. Execute o backend:
   ```bash
   cd backend
   ./mvnw quarkus:dev
   ```

3. Execute o frontend:
   ```bash
   cd frontend
   flutter run
   ```
