-- O BANCO DE DADOS JÁ FOI CRIADO.

-- O NOME DO BANCO DE DADOS É O RA DO ALUNO SEM TRAÇO ("-"). EXEMPLO RA => T12345-6 => T123456 (<RA DO ALUNO> => T123456)

-- DROP DATABASE IF EXISTS <RA DO ALUNO>;

-- CREATE DATABASE IF NOT EXISTS <RA DO ALUNO> CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- USE <RA_DO_ALUNO>;


-- TABELA: alunos

CREATE TABLE alunos (
ra varchar(8) NOT NULL,
nome VARCHAR(100) NOT NULL,
data_nascimento DATE NOT NULL,
rg VARCHAR(20) NOT NULL,
PRIMARY KEY (ra),
CONSTRAINT uk_alunos_rg UNIQUE (rg)
) ENGINE=InnoDB character set utf8mb4 collate utf8mb4_unicode_ci;


-- TABELA: disciplinas

CREATE TABLE disciplinas (
id_disciplina INT NOT NULL AUTO_INCREMENT,
nome_disciplina VARCHAR(100) NOT NULL,
carga_horaria INT NOT NULL,
PRIMARY KEY (id_disciplina),
CONSTRAINT uk_disciplinas_nome UNIQUE (nome_disciplina),
CONSTRAINT ck_disciplinas_carga CHECK (carga_horaria > 0)
) ENGINE=InnoDB character set utf8mb4 collate utf8mb4_unicode_ci;



-- TABELA: tipos_provas

CREATE TABLE tipos_provas (
id_tipo_prova INT NOT NULL AUTO_INCREMENT,
nome_prova VARCHAR(20) NOT NULL,
PRIMARY KEY (id_tipo_prova),
CONSTRAINT uk_tipos_provas_nome UNIQUE (nome_prova)
) ENGINE=InnoDB character set utf8mb4 collate utf8mb4_unicode_ci;



-- TABELA: notas

CREATE TABLE notas (
ra VARCHAR(8) NOT NULL,
id_disciplina INT NOT NULL,
id_tipo_prova INT NOT NULL,
nota DECIMAL(4,2) NOT NULL,
PRIMARY KEY (ra, id_disciplina, id_tipo_prova),
CONSTRAINT fk_notas_aluno FOREIGN KEY (ra) REFERENCES alunos (ra) ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT fk_notas_disciplina FOREIGN KEY (id_disciplina) REFERENCES disciplinas (id_disciplina) ON UPDATE CASCADE ON DELETE RESTRICT,
CONSTRAINT fk_notas_tipo_prova FOREIGN KEY (id_tipo_prova) REFERENCES tipos_provas (id_tipo_prova) ON UPDATE CASCADE ON DELETE RESTRICT,
CONSTRAINT ck_notas_nota CHECK (nota >= 0 AND nota <= 10)
) ENGINE=InnoDB character set utf8mb4 collate utf8mb4_unicode_ci;
