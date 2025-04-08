
CREATE DATABASE IF NOT EXISTS db_s2_ETU003183;
USE db_s2_ETU003183;

CREATE TABLE Previsions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    categorie VARCHAR(100) NOT NULL UNIQUE,
    montant DOUBLE NOT NULL CHECK (montant >= 0),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE Depenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    categorie VARCHAR(100) NOT NULL,
    montant DOUBLE NOT NULL CHECK (montant >= 0),
    date_depense TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (categorie) REFERENCES Previsions(categorie) ON DELETE CASCADE
);
  

INSERT INTO Previsions (categorie, montant) VALUES 
('Nourriture', 500.00),
('Transport', 200.00),
('Loisirs', 150.00);

INSERT INTO Depenses (categorie, montant) VALUES 
('Nourriture', 50.00),
('Transport', 30.00),
('Nourriture', 75.00);


