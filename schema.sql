CREATE DATABASE IF NOT EXISTS sistema_apostas;
USE sistema_apostas;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    saldo DOUBLE NOT NULL DEFAULT 0.0,
    ehAdmin BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE eventos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    timeCasa VARCHAR(100) NOT NULL,
    timeFora VARCHAR(100) NOT NULL,
    oddCasa DOUBLE NOT NULL,
    oddEmpate DOUBLE NOT NULL,
    oddFora DOUBLE NOT NULL,
    status ENUM('ABERTO', 'ENCERRADO') NOT NULL DEFAULT 'ABERTO',
    resultadoFinal ENUM('CASA', 'EMPATE', 'FORA') NULL
);

CREATE TABLE apostas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuarioId INT NOT NULL,
    eventoId INT NOT NULL,
    valorApostado DOUBLE NOT NULL,
    palpite ENUM('CASA', 'EMPATE', 'FORA') NOT NULL,
    status ENUM('PENDENTE', 'GANHA', 'PERDIDA') NOT NULL DEFAULT 'PENDENTE',
    ganhoPotencial DOUBLE NOT NULL,
    FOREIGN KEY (usuarioId) REFERENCES usuarios(id),
    FOREIGN KEY (eventoId) REFERENCES eventos(id)
);

USE sistema_apostas;

-- --------------------------------------------------------
-- 1. Inserindo Usuários
-- Observação: Em um sistema real, as senhas devem ser armazenadas com hash (ex: bcrypt).
-- --------------------------------------------------------
INSERT INTO usuarios (nome, login, senha, saldo, ehAdmin) VALUES
('Administrador Geral', 'admin', 'admin', 10000.00, TRUE),
('João da Silva', 'joao.silva', 'joao', 250.00, FALSE),
('Maria Souza', 'maria.souza', 'hash_senha_maria_123', 500.00, FALSE),
('Carlos Ferreira', 'carlos.f', 'hash_senha_carlos_123', 50.50, FALSE);

-- --------------------------------------------------------
-- 2. Inserindo Eventos
-- --------------------------------------------------------
INSERT INTO eventos (timeCasa, timeFora, oddCasa, oddEmpate, oddFora, status, resultadoFinal) VALUES
('Flamengo', 'Vasco', 1.85, 3.40, 4.20, 'ABERTO', NULL),
('Corinthians', 'Palmeiras', 2.80, 3.10, 2.50, 'ENCERRADO', 'FORA'),
('São Paulo', 'Santos', 2.00, 3.30, 3.50, 'ENCERRADO', 'EMPATE'),
('Cruzeiro', 'Atlético-MG', 2.60, 3.20, 2.70, 'ABERTO', NULL);

-- --------------------------------------------------------
-- 3. Inserindo Apostas
-- Observação: O ganhoPotencial é calculado como (valorApostado * oddEscolhida).
-- --------------------------------------------------------
-- Aposta 1: João aposta 50 no Flamengo (CASA) no Evento 1 (Odd 1.85) -> Ganho = 92.50
INSERT INTO apostas (usuarioId, eventoId, valorApostado, palpite, status, ganhoPotencial) 
VALUES (2, 1, 50.00, 'CASA', 'PENDENTE', 92.50);

-- Aposta 2: Maria aposta 100 no Palmeiras (FORA) no Evento 2 (Odd 2.50) -> Ganho = 250.00
-- O evento já encerrou e o Palmeiras venceu, então a aposta está GANHA.
INSERT INTO apostas (usuarioId, eventoId, valorApostado, palpite, status, ganhoPotencial) 
VALUES (3, 2, 100.00, 'FORA', 'GANHA', 250.00);

-- Aposta 3: João aposta 30 no São Paulo (CASA) no Evento 3 (Odd 2.00) -> Ganho = 60.00
-- O evento encerrou em EMPATE, então a aposta está PERDIDA.
INSERT INTO apostas (usuarioId, eventoId, valorApostado, palpite, status, ganhoPotencial) 
VALUES (2, 3, 30.00, 'CASA', 'PERDIDA', 60.00);

-- Aposta 4: Carlos aposta 20 no Empate do Evento 4 (Odd 3.20) -> Ganho = 64.00
INSERT INTO apostas (usuarioId, eventoId, valorApostado, palpite, status, ganhoPotencial) 
VALUES (4, 4, 20.00, 'EMPATE', 'PENDENTE', 64.00);

-- Aposta 5: Maria aposta 200 no Vasco (FORA) no Evento 1 (Odd 4.20) -> Ganho = 840.00
INSERT INTO apostas (usuarioId, eventoId, valorApostado, palpite, status, ganhoPotencial) 
VALUES (3, 1, 200.00, 'FORA', 'PENDENTE', 840.00);