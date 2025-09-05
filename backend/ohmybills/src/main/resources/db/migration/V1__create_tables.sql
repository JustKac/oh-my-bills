-- Tabela principal de usuário
CREATE TABLE tb_user (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    keycloak_id UUID UNIQUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    canceled_at TIMESTAMP
);

-- Tabela de cartão de crédito
CREATE TABLE tb_credit_card (
    id UUID PRIMARY KEY,
    credit_limit NUMERIC(10,2),
    due_date DATE,
    best_shopping_day DATE,
    name VARCHAR(255),
    last_four_digits CHAR(4),
    brand VARCHAR(50),
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES tb_user(id)
);

-- Tabela de fatura
CREATE TABLE tb_invoice (
    id UUID PRIMARY KEY,
    name VARCHAR(100),
    credit_card_id UUID,
    FOREIGN KEY (credit_card_id) REFERENCES tb_credit_card(id)
);

-- Tabela de tag de pessoa
CREATE TABLE tb_tag (
    id UUID PRIMARY KEY,
    is_person BOOLEAN,
    color VARCHAR(7),
    name VARCHAR(100),
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES tb_user(id)
);

-- Tabela de receita
CREATE TABLE tb_income (
    id UUID PRIMARY KEY,
    description VARCHAR(100),
    amount NUMERIC(10,2),
    first_pay_date DATE,
    is_recurring BOOLEAN,
    installments INTEGER,
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES tb_user(id)
);

-- Tabela de despesa
CREATE TABLE tb_expense (
    id UUID PRIMARY KEY,
    description VARCHAR(100),
    amount NUMERIC(10,2),
    first_pay_date DATE,
    is_recurring BOOLEAN,
    is_archived BOOLEAN,
    installments INTEGER,
    user_id UUID NOT NULL,
    credit_card_id UUID,
    invoice_id UUID,
    FOREIGN KEY (credit_card_id) REFERENCES tb_credit_card(id),
    FOREIGN KEY (user_id) REFERENCES tb_user(id),
    FOREIGN KEY (invoice_id) REFERENCES tb_invoice(id)
);

-- Tabela para armazenar avatares de usuário
CREATE TABLE tb_user_avatar (
    user_id UUID PRIMARY KEY REFERENCES tb_user(id) ON DELETE CASCADE,
    content_type VARCHAR(100) NOT NULL,
    data bytea NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- Tabela de junção ManyToMany entre Expense e Tag
CREATE TABLE tb_expense_tag (
    expense_id UUID NOT NULL,
    tag_id UUID NOT NULL,
    PRIMARY KEY (expense_id, tag_id),
    FOREIGN KEY (expense_id) REFERENCES tb_expense(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tb_tag(id) ON DELETE CASCADE
);