drop database if exists hoop_alert;
create database hoop_alert;
use hoop_alert;

create table Users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE teams (
    id INT AUTO_INCREMENT PRIMARY KEY, -- Unique ID for internal use
    team_id INT NOT NULL UNIQUE, -- Unique identifier for the team
    team_city VARCHAR(50), -- City of the team
    team_name VARCHAR(100), -- Name of the team
    conference VARCHAR(50), -- Conference name
    conference_record VARCHAR(10), -- Conference record (e.g., 41-11)
    playoff_rank INT, -- Playoff rank
    clinch_indicator VARCHAR(10), -- Clinch indicator (e.g., - e, - w)
    division VARCHAR(50), -- Division name
    division_record VARCHAR(10), -- Division record (e.g., 15-2)
    season_id INT, -- Season ID
    league_id INT, -- League ID (constant for NBA)
    CONSTRAINT unique_team UNIQUE (team_id, season_id) -- Ensure unique team per season
);

create table TeamStats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nba_team_id BIGINT NOT NULL,
    season_year INT NOT NULL,
    wins INT,
    losses INT,
    points_per_game DECIMAL(5, 2),
    rebounds_per_game DECIMAL(5, 2),
    assists_per_game DECIMAL(5, 2),
    FOREIGN KEY (nba_team_id) REFERENCES Teams(nba_team_id)
);

create table Games (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nba_game_id BIGINT UNIQUE NOT NULL,  -- ID from the NBA API
    nba_team_id_home BIGINT NOT NULL,
    nba_team_id_away BIGINT NOT NULL,
    season_year INT NOT NULL,
    date DATE,
    time TIME,
    venue VARCHAR(255),
    home_team_score INT,
    away_team_score INT,
    FOREIGN KEY (nba_team_id_home) REFERENCES Teams(nba_team_id),
    FOREIGN KEY (nba_team_id_away) REFERENCES Teams(nba_team_id)
);

create table UserReminders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    nba_game_id BIGINT NOT NULL,
    reminder_time TIMESTAMP,
    sent BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (nba_game_id) REFERENCES Games(nba_game_id)
);

