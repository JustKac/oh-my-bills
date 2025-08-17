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
    name VARCHAR(255)
);

-- Tabela de fatura
CREATE TABLE tb_invoice (
    id UUID PRIMARY KEY,
    name VARCHAR(100),
    credit_card UUID,
    FOREIGN KEY (credit_card) REFERENCES tb_credit_card(id)
);

-- Tabela de tag de pessoa
CREATE TABLE tb_person_tag (
    id UUID PRIMARY KEY,
    name VARCHAR(100)
);

-- Tabela de receita
CREATE TABLE tb_income (
    id UUID PRIMARY KEY,
    description VARCHAR(100),
    amount NUMERIC(10,2),
    date DATE,
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
    date DATE,
    is_recurring BOOLEAN,
    is_achived BOOLEAN,
    installments INTEGER,
    user_id UUID NOT NULL,
    invoice_id UUID,
    person_tag_id UUID,
    FOREIGN KEY (user_id) REFERENCES tb_user(id),
    FOREIGN KEY (invoice_id) REFERENCES tb_invoice(id),
    FOREIGN KEY (person_tag_id) REFERENCES tb_person_tag(id)
);