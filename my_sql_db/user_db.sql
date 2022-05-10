CREATE  DATABASE IF NOT EXISTS `users_db`;
CREATE TABLE users_db.User
(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    ssn VARCHAR(30), -- social security number
    nom VARCHAR(100),
    prenom VARCHAR(100),
    date_naissance VARCHAR(30),
    email VARCHAR(255),
    tel_num VARCHAR(30),
    id_remboursement INT,
    code_soin VARCHAR(10),
    montant_remboursement FLOAT,
    timestamp VARCHAR(30)
)
