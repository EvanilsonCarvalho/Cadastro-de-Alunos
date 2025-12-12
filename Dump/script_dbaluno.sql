-- Criação do banco de dados
CREATE DATABASE db_escola;

-- Seleciona o banco
USE db_escola;

-- Criação da tabela de alunos com RA sequencial
CREATE TABLE tbalunos (
    ra INT AUTO_INCREMENT PRIMARY KEY,         -- RA gerado automaticamente
    nome VARCHAR(100) NOT NULL,
    endereco VARCHAR(150),
    telefone VARCHAR(20),
    email VARCHAR(100),
    curso VARCHAR(100),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cpf VARCHAR(14) UNIQUE,
    status VARCHAR(20) DEFAULT 'Ativo'
);

select * from tbalunos;

--  Exibe a estrutura da tabela tbalunos (colunas, tipos, restrições, etc.)
DESCRIBE tbalunos;

-- Inserts sem informar o RA (será gerado automaticamente)
-- Inserindo alunos com datas variadas de cadastro
INSERT INTO tbalunos (nome, endereco, telefone, email, curso, cpf, data_cadastro)
VALUES
('João da Silva', 'Rua das Flores, 123', '(11) 99999-9999', 'joao.silva@email.com', 'ADS', '123.456.789-00', '2025-01-10');

--  Adiciona a coluna 'status' à tabela, com valor padrão 'Ativo'
-- ALTER TABLE tbalunos ADD COLUMN status VARCHAR(20) DEFAULT 'Ativo';

--  Adiciona a coluna 'cpf' à tabela, com tamanho máximo de 14 caracteres e valor único
-- ALTER TABLE tbalunos ADD COLUMN cpf VARCHAR(14) UNIQUE;



--  Exibe todos os registros da tabela tbalunos
SELECT * FROM tbalunos;

--  Lista todas as tabelas existentes no banco de dados atual
SHOW TABLES;

--  Busca o registro do aluno cujo RA é igual a 1
 SELECT * FROM tbalunos WHERE ra = 1;

-- Seleciona todos os dados da tabela 'tbalunos' onde o valor da coluna 'ra' é igual ao parâmetro fornecido '$P{ra}'.
-- Esse parâmetro foi usado em ferramentas de relatórios (JasperReports) para permitir filtros dinâmicos.
-- SELECT * FROM tbalunos WHERE ra = $P{ra};

