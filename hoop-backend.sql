drop database if exists hoop_alert;
create database hoop_alert;
use hoop_alert;
DROP TABLE IF EXISTS user_team;
DROP TABLE IF EXISTS reminders;
DROP TABLE IF EXISTS teams;
DROP TABLE IF EXISTS users;

create table users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash varchar(2048) not null,
    enabled bit not null default(1),
    phone_number VARCHAR(20),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT pk_user_role PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_users FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES role(id)
);


CREATE TABLE teams (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    team_name VARCHAR(100) NOT NULL,
    team_city VARCHAR(100),
    team_abbreviation VARCHAR(10),
    nba_team_id VARCHAR(10) NOT NULL,  -- Corresponds to the NBA API team ID
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_team (
    user_id BIGINT,
    team_id BIGINT,
    PRIMARY KEY(user_id, team_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (team_id) REFERENCES teams(id)
);


CREATE TABLE reminders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reminder_time TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (team_id) REFERENCES teams(id)
);

INSERT INTO role (name) VALUES ('USER'), ('ADMIN');
