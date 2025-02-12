-- Tabela de Usuário
CREATE TABLE User (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    created_at TEXT NOT NULL DEFAULT (datetime('now'))
);

-- Tabela de Categorias
CREATE TABLE Category (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    type TEXT NOT NULL CHECK (type IN ('income', 'expense')),
    FOREIGN KEY (user_id) REFERENCES User(id)
);

-- Tabela de Transações (Receitas/Despesas)
CREATE TABLE Transaction (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    category_id INTEGER,
    amount REAL NOT NULL CHECK (amount > 0),
    date TEXT NOT NULL,
    type TEXT NOT NULL CHECK (type IN ('income', 'expense')),
    description TEXT,
    is_recurring INTEGER NOT NULL DEFAULT 0, -- 0 = Não, 1 = Sim
    recurring_interval TEXT, -- Ex: "monthly", "weekly"
    installment_group_id INTEGER, -- Grupo de parcelamento (se aplicável)
    installment_number INTEGER, -- Número da parcela (ex: 1/12)
    credit_card_id INTEGER, -- Transação associada a um cartão
    bill_id INTEGER, -- Transação associada a um boleto
    savings_goal_id INTEGER, -- Transação associada a uma poupança
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (category_id) REFERENCES Category(id),
    FOREIGN KEY (installment_group_id) REFERENCES InstallmentGroup(id),
    FOREIGN KEY (credit_card_id) REFERENCES CreditCard(id),
    FOREIGN KEY (bill_id) REFERENCES Bill(id),
    FOREIGN KEY (savings_goal_id) REFERENCES SavingsGoal(id)
);

-- Tabela de Cartões de Crédito
CREATE TABLE CreditCard (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    name TEXT NOT NULL, -- Ex: "Nubank", "Itaú"
    credit_limit REAL NOT NULL,
    closing_day INTEGER NOT NULL, -- Dia de fechamento (ex: 5)
    due_day INTEGER NOT NULL, -- Dia de vencimento (ex: 10)
    FOREIGN KEY (user_id) REFERENCES User(id)
);

-- Tabela de Faturas do Cartão
CREATE TABLE CreditCardStatement (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    credit_card_id INTEGER NOT NULL,
    closing_date TEXT NOT NULL, -- Data de fechamento (ex: "2024-03-05")
    due_date TEXT NOT NULL, -- Data de vencimento (ex: "2024-03-10")
    total_amount REAL NOT NULL,
    is_paid INTEGER NOT NULL DEFAULT 0, -- 0 = Não paga, 1 = Paga
    FOREIGN KEY (credit_card_id) REFERENCES CreditCard(id)
);

-- Tabela de Boletos
CREATE TABLE Bill (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    name TEXT NOT NULL, -- Ex: "Conta de Luz"
    due_date TEXT NOT NULL,
    amount REAL NOT NULL,
    is_paid INTEGER NOT NULL DEFAULT 0, -- 0 = Não pago, 1 = Pago
    FOREIGN KEY (user_id) REFERENCES User(id)
);

-- Tabela de Empréstimos
CREATE TABLE Loan (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    total_amount REAL NOT NULL,
    total_installments INTEGER NOT NULL,
    start_date TEXT NOT NULL,
    description TEXT,
    FOREIGN KEY (user_id) REFERENCES User(id)
);

-- Tabela de Parcelas de Empréstimo
CREATE TABLE LoanInstallment (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    loan_id INTEGER NOT NULL,
    due_date TEXT NOT NULL,
    amount REAL NOT NULL,
    is_paid INTEGER NOT NULL DEFAULT 0, -- 0 = Não paga, 1 = Paga
    installment_number INTEGER NOT NULL, -- Ex: 1/12
    FOREIGN KEY (loan_id) REFERENCES Loan(id)
);

-- Tabela de Poupanças (Metas Financeiras)
CREATE TABLE SavingsGoal (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    name TEXT NOT NULL, -- Ex: "Viagem para Europa"
    target_amount REAL NOT NULL,
    current_amount REAL NOT NULL DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES User(id)
);

-- Tabela de Transações de Poupança
CREATE TABLE SavingsTransaction (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    savings_goal_id INTEGER NOT NULL,
    amount REAL NOT NULL,
    date TEXT NOT NULL,
    type TEXT NOT NULL CHECK (type IN ('deposit', 'withdrawal')),
    FOREIGN KEY (savings_goal_id) REFERENCES SavingsGoal(id)
);

-- Tabela de Grupos de Parcelamento (para despesas em múltiplas parcelas)
CREATE TABLE InstallmentGroup (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    total_amount REAL NOT NULL,
    total_installments INTEGER NOT NULL,
    start_date TEXT NOT NULL,
    description TEXT,
    FOREIGN KEY (user_id) REFERENCES User(id)
);