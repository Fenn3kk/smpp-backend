CREATE TABLE Usuario (
                         id UUID PRIMARY KEY,
                         nome TEXT NOT NULL,
                         email TEXT NOT NULL UNIQUE,
                         telefone TEXT NOT NULL,
                         senha TEXT NOT NULL,
                         tipo_usuario TEXT CHECK (tipo_usuario IN ('ADMIN', 'COMUM'))
);

CREATE TABLE Cidade (
                        id UUID PRIMARY KEY,
                        nome TEXT NOT NULL
);

CREATE TABLE Atividade (
                           id UUID PRIMARY KEY,
                           nome TEXT NOT NULL
);

CREATE TABLE Vulnerabilidade (
                                 id UUID PRIMARY KEY,
                                 nome TEXT NOT NULL
);

CREATE TABLE Propriedade (
                             id UUID PRIMARY KEY,
                             nome TEXT NOT NULL,
                             cidade_id UUID NOT NULL,
                             coordenadas TEXT NOT NULL,
                             proprietario TEXT NOT NULL,
                             telefone_proprietario TEXT NOT NULL,
                             usuario_id UUID,
                             CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE CASCADE,
                             CONSTRAINT fk_cidade FOREIGN KEY (cidade_id) REFERENCES Cidade(id) ON DELETE RESTRICT
);

CREATE TABLE propriedade_atividade (
                                       propriedade_id UUID NOT NULL,
                                       atividade_id UUID NOT NULL,
                                       PRIMARY KEY (propriedade_id, atividade_id),
                                       FOREIGN KEY (propriedade_id) REFERENCES Propriedade(id) ON DELETE CASCADE,
                                       FOREIGN KEY (atividade_id) REFERENCES Atividade(id) ON DELETE CASCADE
);

CREATE TABLE propriedade_vulnerabilidade (
                                             propriedade_id UUID NOT NULL,
                                             vulnerabilidade_id UUID NOT NULL,
                                             PRIMARY KEY (propriedade_id, vulnerabilidade_id),
                                             FOREIGN KEY (propriedade_id) REFERENCES Propriedade(id) ON DELETE CASCADE,
                                             FOREIGN KEY (vulnerabilidade_id) REFERENCES Vulnerabilidade(id) ON DELETE CASCADE
);
