-- Convenção: Nomes de tabelas e colunas em snake_case (minusculo_com_underscore)
-- É o padrão mais comum em PostgreSQL e evita problemas com letras maiúsculas.

-- 1. Tipos Customizados (ENUM)
CREATE TYPE tipo_usuario_enum AS ENUM ('ADMIN', 'COMUM');

-- 2. Tabelas Principais
CREATE TABLE usuario (
                         id UUID PRIMARY KEY,
                         nome VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         telefone VARCHAR(20) NOT NULL,
                         senha VARCHAR(72) NOT NULL,
                         tipo_usuario tipo_usuario_enum NOT NULL
);

CREATE TABLE cidade (
                        id UUID PRIMARY KEY,
                        nome VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE atividade (
                           id UUID PRIMARY KEY,
                           nome VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE vulnerabilidade (
                                 id UUID PRIMARY KEY,
                                 nome VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE tipo_ocorrencia (
                                 id UUID PRIMARY KEY,
                                 nome VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE incidente (
                           id UUID PRIMARY KEY,
                           nome VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE propriedade (
                             id UUID PRIMARY KEY,
                             nome VARCHAR(255) NOT NULL,
                             cidade_id UUID NOT NULL,
                             coordenadas POINT NOT NULL,
                             proprietario VARCHAR(255) NOT NULL,
                             telefone_proprietario VARCHAR(20) NOT NULL,
                             usuario_id UUID,
                             CONSTRAINT fk_propriedade_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE SET NULL,
                             CONSTRAINT fk_propriedade_cidade FOREIGN KEY (cidade_id) REFERENCES cidade(id) ON DELETE RESTRICT
);

CREATE TABLE ocorrencia (
                            id UUID PRIMARY KEY,
                            tipo_ocorrencia_id UUID NOT NULL,
                            data DATE NOT NULL,
                            descricao TEXT,
                            propriedade_id UUID NOT NULL,
                            CONSTRAINT fk_ocorrencia_propriedade FOREIGN KEY (propriedade_id) REFERENCES propriedade(id) ON DELETE CASCADE,
                            CONSTRAINT fk_ocorrencia_tipo FOREIGN KEY (tipo_ocorrencia_id) REFERENCES tipo_ocorrencia(id) ON DELETE RESTRICT
);

CREATE TABLE foto_ocorrencia (
                                 id UUID PRIMARY KEY,
                                 caminho VARCHAR(255) NOT NULL,
                                 nome VARCHAR(255),
                                 ocorrencia_id UUID NOT NULL,
                                 CONSTRAINT fk_foto_ocorrencia_ocorrencia FOREIGN KEY(ocorrencia_id) REFERENCES ocorrencia(id) ON DELETE CASCADE
);

-- 3. Tabelas de Junção (Muitos-para-Muitos)
CREATE TABLE propriedade_atividade (
                                       propriedade_id UUID NOT NULL,
                                       atividade_id UUID NOT NULL,
                                       PRIMARY KEY (propriedade_id, atividade_id),
                                       FOREIGN KEY (propriedade_id) REFERENCES propriedade(id) ON DELETE CASCADE,
                                       FOREIGN KEY (atividade_id) REFERENCES atividade(id) ON DELETE CASCADE
);

CREATE TABLE propriedade_vulnerabilidade (
                                             propriedade_id UUID NOT NULL,
                                             vulnerabilidade_id UUID NOT NULL,
                                             PRIMARY KEY (propriedade_id, vulnerabilidade_id),
                                             FOREIGN KEY (propriedade_id) REFERENCES propriedade(id) ON DELETE CASCADE,
                                             FOREIGN KEY (vulnerabilidade_id) REFERENCES vulnerabilidade(id) ON DELETE CASCADE
);

CREATE TABLE ocorrencia_incidente (
                                      ocorrencia_id UUID NOT NULL,
                                      incidente_id UUID NOT NULL,
                                      PRIMARY KEY (ocorrencia_id, incidente_id),
                                      FOREIGN KEY (ocorrencia_id) REFERENCES ocorrencia(id) ON DELETE CASCADE,
                                      FOREIGN KEY (incidente_id) REFERENCES incidente(id) ON DELETE CASCADE
);

-- 4. Índices para Performance de Consultas
CREATE INDEX idx_propriedade_cidade_id ON propriedade(cidade_id);
CREATE INDEX idx_propriedade_usuario_id ON propriedade(usuario_id);
CREATE INDEX idx_ocorrencia_propriedade_id ON ocorrencia(propriedade_id);
CREATE INDEX idx_ocorrencia_tipo_ocorrencia_id ON ocorrencia(tipo_ocorrencia_id);
CREATE INDEX idx_foto_ocorrencia_ocorrencia_id ON foto_ocorrencia(ocorrencia_id);