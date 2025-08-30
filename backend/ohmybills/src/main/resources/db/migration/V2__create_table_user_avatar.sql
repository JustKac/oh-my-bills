-- Cria a tabela para armazenar avatares de usuário
CREATE TABLE tb_user_avatar (
    user_id UUID PRIMARY KEY REFERENCES tb_user(id) ON DELETE CASCADE,
    content_type VARCHAR(100) NOT NULL,
    data bytea NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);