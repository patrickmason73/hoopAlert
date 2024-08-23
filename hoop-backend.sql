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
    phone_number VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
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
    team_logo_url VARCHAR(255),
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
    reminder_time DATETIME NOT NULL,
    user_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (team_id) REFERENCES teams(id)
);

CREATE TABLE schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    home_team_id BIGINT NOT NULL,
    away_team_id BIGINT NOT NULL,
    game_date DATETIME NOT NULL,
    location VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (home_team_id) REFERENCES teams(id),
    FOREIGN KEY (away_team_id) REFERENCES teams(id)
);

INSERT INTO role (name) VALUES ('USER'), ('ADMIN');

insert into users (username, password_hash, enabled, phone_number, email)
    values
    ('john@smith.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1, 1234567890, 'john@smith.com'),
    ('sally@jones.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 1, 9876543210, 'sally@jones.com');
    
INSERT INTO teams (team_name, team_city, team_abbreviation, nba_team_id, team_logo_url)
VALUES
('Hawks', 'Atlanta', 'ATL', '1610612737', 'https://a.espncdn.com/i/teamlogos/nba/500/atl.png'),
('Celtics', 'Boston', 'BOS', '1610612738', 'https://a.espncdn.com/i/teamlogos/nba/500/bos.png'),
('Nets', 'Brooklyn', 'BKN', '1610612751', 'https://a.espncdn.com/i/teamlogos/nba/500/bkn.png'),
('Hornets', 'Charlotte', 'CHA', '1610612766', 'https://a.espncdn.com/i/teamlogos/nba/500/cha.png'),
('Bulls', 'Chicago', 'CHI', '1610612741', 'https://a.espncdn.com/i/teamlogos/nba/500/chi.png'),
('Cavaliers', 'Cleveland', 'CLE', '1610612739', 'https://a.espncdn.com/i/teamlogos/nba/500/cle.png'),
('Mavericks', 'Dallas', 'DAL', '1610612742', 'https://a.espncdn.com/i/teamlogos/nba/500/dal.png'),
('Nuggets', 'Denver', 'DEN', '1610612743', 'https://a.espncdn.com/i/teamlogos/nba/500/den.png'),
('Pistons', 'Detroit', 'DET', '1610612765', 'https://a.espncdn.com/i/teamlogos/nba/500/det.png'),
('Warriors', 'Golden State', 'GSW', '1610612744', 'https://a.espncdn.com/i/teamlogos/nba/500/gs.png'),
('Rockets', 'Houston', 'HOU', '1610612745', 'https://a.espncdn.com/i/teamlogos/nba/500/hou.png'),
('Pacers', 'Indiana', 'IND', '1610612754', 'https://a.espncdn.com/i/teamlogos/nba/500/ind.png'),
('Clippers', 'LA', 'LAC', '1610612746', 'https://a.espncdn.com/i/teamlogos/nba/500/lac.png'),
('Lakers', 'Los Angeles', 'LAL', '1610612747', 'https://a.espncdn.com/i/teamlogos/nba/500/lal.png'),
('Grizzlies', 'Memphis', 'MEM', '1610612763', 'https://a.espncdn.com/i/teamlogos/nba/500/mem.png'),
('Heat', 'Miami', 'MIA', '1610612748', 'https://a.espncdn.com/i/teamlogos/nba/500/mia.png'),
('Bucks', 'Milwaukee', 'MIL', '1610612749', 'https://a.espncdn.com/i/teamlogos/nba/500/mil.png'),
('Timberwolves', 'Minnesota', 'MIN', '1610612750', 'https://a.espncdn.com/i/teamlogos/nba/500/min.png'),
('Pelicans', 'New Orleans', 'NOP', '1610612740', 'https://a.espncdn.com/i/teamlogos/nba/500/no.png'),
('Knicks', 'New York', 'NYK', '1610612752', 'https://a.espncdn.com/i/teamlogos/nba/500/ny.png'),
('Thunder', 'Oklahoma City', 'OKC', '1610612760', 'https://a.espncdn.com/i/teamlogos/nba/500/okc.png'),
('Magic', 'Orlando', 'ORL', '1610612753', 'https://a.espncdn.com/i/teamlogos/nba/500/orl.png'),
('76ers', 'Philadelphia', 'PHI', '1610612755', 'https://a.espncdn.com/i/teamlogos/nba/500/phi.png'),
('Suns', 'Phoenix', 'PHX', '1610612756', 'https://a.espncdn.com/i/teamlogos/nba/500/phx.png'),
('Trail Blazers', 'Portland', 'POR', '1610612757', 'https://a.espncdn.com/i/teamlogos/nba/500/por.png'),
('Kings', 'Sacramento', 'SAC', '1610612758', 'https://a.espncdn.com/i/teamlogos/nba/500/sac.png'),
('Spurs', 'San Antonio', 'SAS', '1610612759', 'https://a.espncdn.com/i/teamlogos/nba/500/sa.png'),
('Raptors', 'Toronto', 'TOR', '1610612761', 'https://a.espncdn.com/i/teamlogos/nba/500/tor.png'),
('Jazz', 'Utah', 'UTA', '1610612762', 'https://a.espncdn.com/i/teamlogos/nba/500/utah.png'),
('Wizards', 'Washington', 'WAS', '1610612764', 'https://a.espncdn.com/i/teamlogos/nba/500/wsh.png');

-- Games for August 22, 2024 (Thursday)
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES
(2, 1, '2024-08-22 19:00:00', 'TD Garden'),
(4, 3, '2024-08-22 19:30:00', 'Barclays Center'),
(6, 5, '2024-08-22 20:00:00', 'Toyota Center'),
(8, 7, '2024-08-22 20:30:00', 'Pepsi Center'),
(10, 9, '2024-08-22 21:00:00', 'Little Caesars Arena'),
(12, 11, '2024-08-22 21:30:00', 'Chase Center'),
(14, 13, '2024-08-22 22:00:00', 'Staples Center'),
(16, 15, '2024-08-22 22:30:00', 'American Airlines Arena'),
(18, 17, '2024-08-22 19:00:00', 'Spectrum Center'),
(20, 19, '2024-08-22 19:30:00', 'Capital One Arena'),
(22, 21, '2024-08-22 20:00:00', 'Amway Center'),
(24, 23, '2024-08-22 20:30:00', 'Madison Square Garden'),
(26, 25, '2024-08-22 21:00:00', 'Wells Fargo Center'),
(28, 27, '2024-08-22 21:30:00', 'Vivint Arena'),
(30, 29, '2024-08-22 22:00:00', 'FedExForum');

-- Games for August 23, 2024 (Friday)
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES
(1, 2, '2024-08-23 19:00:00', 'State Farm Arena'),
(3, 4, '2024-08-23 19:30:00', 'Barclays Center'),
(5, 6, '2024-08-23 20:00:00', 'United Center'),
(7, 8, '2024-08-23 20:30:00', 'Pepsi Center'),
(9, 10, '2024-08-23 21:00:00', 'Little Caesars Arena'),
(11, 12, '2024-08-23 21:30:00', 'Chase Center'),
(13, 14, '2024-08-23 22:00:00', 'Staples Center'),
(15, 16, '2024-08-23 22:30:00', 'American Airlines Arena'),
(17, 18, '2024-08-23 19:00:00', 'Fiserv Forum'),
(19, 20, '2024-08-23 19:30:00', 'Target Center'),
(21, 22, '2024-08-23 20:00:00', 'Smoothie King Center'),
(23, 24, '2024-08-23 20:30:00', 'Madison Square Garden'),
(25, 26, '2024-08-23 21:00:00', 'Amway Center'),
(27, 28, '2024-08-23 21:30:00', 'Wells Fargo Center'),
(29, 30, '2024-08-23 22:00:00', 'Vivint Arena');


INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (2, 20, '2024-10-22 19:30:00', 'TD Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (14, 18, '2024-10-22 22:00:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (9, 12, '2024-10-23 19:00:00', 'Little Caesars Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (1, 3, '2024-10-23 19:30:00', 'State Farm Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (16, 22, '2024-10-23 19:30:00', 'FTX Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (23, 17, '2024-10-23 19:30:00', 'Wells Fargo Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (28, 6, '2024-10-23 19:30:00', 'Scotiabank Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (11, 4, '2024-10-23 20:00:00', 'Toyota Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (19, 5, '2024-10-23 20:00:00', 'Smoothie King Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (29, 15, '2024-10-23 21:00:00', 'Vivint Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (13, 24, '2024-10-23 22:00:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (25, 10, '2024-10-23 22:00:00', 'Moda Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (2, 4, '2024-10-24 19:00:00', 'TD Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (6, 16, '2024-10-24 19:30:00', 'Rocket Mortgage FieldHouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (18, 17, '2024-10-24 19:30:00', 'Target Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (8, 12, '2024-10-24 20:00:00', 'Ball Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (7, 14, '2024-10-24 20:30:00', 'American Airlines Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (22, 13, '2024-10-24 22:00:00', 'Amway Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (28, 21, '2024-10-24 22:30:00', 'Scotiabank Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (30, 19, '2024-10-25 19:00:00', 'Capital One Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (1, 5, '2024-10-25 19:30:00', 'State Farm Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (26, 9, '2024-10-25 20:00:00', 'Golden 1 Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (12, 15, '2024-10-25 20:30:00', 'Gainbridge Fieldhouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (10, 23, '2024-10-25 21:00:00', 'Chase Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (11, 24, '2024-10-25 22:00:00', 'Toyota Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (20, 3, '2024-10-26 19:30:00', 'Madison Square Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (18, 7, '2024-10-26 20:00:00', 'Target Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (14, 8, '2024-10-26 20:30:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (17, 13, '2024-10-26 21:00:00', 'Fiserv Forum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (16, 27, '2024-10-26 22:30:00', 'Kaseya Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (25, 29, '2024-10-26 23:00:00', 'Moda Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (2, 1, '2024-10-27 15:30:00', 'TD Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (28, 5, '2024-10-27 17:00:00', 'Scotiabank Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (12, 23, '2024-10-27 18:00:00', 'Gainbridge Fieldhouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (19, 4, '2024-10-27 19:00:00', 'Smoothie King Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (22, 10, '2024-10-27 19:30:00', 'Amway Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (11, 26, '2024-10-27 20:00:00', 'Toyota Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (21, 6, '2024-10-27 20:30:00', 'Paycom Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (25, 15, '2024-10-27 22:00:00', 'Moda Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (24, 14, '2024-10-28 22:30:00', 'Footprint Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (29, 9, '2024-10-28 23:00:00', 'Vivint Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (30, 8, '2024-10-29 19:00:00', 'Capital One Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (23, 18, '2024-10-29 19:30:00', 'Wells Fargo Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (6, 13, '2024-10-29 19:30:00', 'Rocket Mortgage FieldHouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (27, 20, '2024-10-29 20:00:00', 'AT&T Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (17, 3, '2024-10-29 21:00:00', 'Fiserv Forum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (16, 21, '2024-10-29 21:30:00', 'Kaseya Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (10, 12, '2024-10-29 22:00:00', 'Chase Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (13, 7, '2024-10-29 22:30:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (11, 2, '2024-10-29 23:00:00', 'Toyota Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (14, 15, '2024-10-30 20:00:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (30, 16, '2024-10-30 21:00:00', 'Capital One Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (18, 24, '2024-10-30 21:30:00', 'Target Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (29, 27, '2024-10-30 22:00:00', 'Vivint Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (8, 5, '2024-10-31 19:00:00', 'Ball Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (22, 25, '2024-10-31 19:30:00', 'Amway Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (2, 30, '2024-10-31 20:00:00', 'TD Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (12, 21, '2024-10-31 20:30:00', 'Gainbridge Fieldhouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (15, 9, '2024-10-31 21:00:00', 'FedExForum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (3, 6, '2024-10-31 21:30:00', 'Barclays Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (20, 28, '2024-10-31 22:00:00', 'Madison Square Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (7, 23, '2024-11-01 19:30:00', 'American Airlines Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (4, 19, '2024-11-01 20:00:00', 'Spectrum Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (10, 8, '2024-11-01 22:00:00', 'Chase Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (17, 29, '2024-11-01 22:30:00', 'Fiserv Forum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (13, 16, '2024-11-01 23:00:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (5, 26, '2024-11-02 20:00:00', 'United Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (25, 24, '2024-11-02 21:00:00', 'Moda Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (14, 9, '2024-11-02 21:30:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (11, 18, '2024-11-02 22:00:00', 'Toyota Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (1, 15, '2024-11-03 19:00:00', 'State Farm Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (7, 13, '2024-11-03 19:30:00', 'American Airlines Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (20, 21, '2024-11-03 20:00:00', 'Madison Square Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (28, 30, '2024-11-03 21:00:00', 'Scotiabank Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (8, 5, '2024-11-03 22:00:00', 'Ball Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (10, 16, '2024-11-03 22:30:00', 'Chase Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (12, 22, '2024-11-03 23:00:00', 'Gainbridge Fieldhouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (17, 4, '2024-11-04 20:00:00', 'Fiserv Forum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (29, 11, '2024-11-04 21:00:00', 'Vivint Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (26, 23, '2024-11-04 22:00:00', 'Golden 1 Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (7, 14, '2024-11-04 22:30:00', 'American Airlines Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (19, 2, '2024-11-05 19:30:00', 'Smoothie King Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (16, 18, '2024-11-05 21:00:00', 'Kaseya Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (27, 30, '2024-11-05 21:30:00', 'AT&T Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (24, 20, '2024-11-05 22:00:00', 'Footprint Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (10, 15, '2024-11-05 22:30:00', 'Chase Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (22, 8, '2024-11-06 19:00:00', 'Amway Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (5, 6, '2024-11-06 20:00:00', 'United Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (30, 25, '2024-11-06 22:00:00', 'Capital One Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (26, 9, '2024-11-06 23:00:00', 'Golden 1 Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (2, 21, '2024-11-07 20:00:00', 'TD Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (4, 23, '2024-11-07 21:00:00', 'Spectrum Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (12, 11, '2024-11-07 21:30:00', 'Gainbridge Fieldhouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (16, 14, '2024-11-07 22:00:00', 'Kaseya Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (19, 29, '2024-11-07 22:30:00', 'Smoothie King Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (17, 10, '2024-11-07 23:00:00', 'Fiserv Forum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (20, 13, '2024-11-08 19:30:00', 'Madison Square Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (1, 30, '2024-11-08 20:00:00', 'State Farm Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (21, 18, '2024-11-08 21:00:00', 'Paycom Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (7, 12, '2024-11-08 21:30:00', 'American Airlines Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (27, 15, '2024-11-08 22:00:00', 'AT&T Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (3, 2, '2024-11-09 19:30:00', 'Barclays Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (9, 4, '2024-11-09 20:00:00', 'Little Caesars Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (11, 5, '2024-11-09 21:00:00', 'Toyota Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (8, 10, '2024-11-09 22:00:00', 'Ball Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (22, 24, '2024-11-09 22:30:00', 'Amway Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (13, 26, '2024-11-09 23:00:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (2, 23, '2024-11-10 19:00:00', 'TD Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (25, 17, '2024-11-10 20:00:00', 'Moda Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (14, 16, '2024-11-10 21:00:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (28, 19, '2024-11-10 22:00:00', 'Scotiabank Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (20, 18, '2024-11-10 22:30:00', 'Madison Square Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (15, 24, '2024-11-10 23:00:00', 'FedExForum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (26, 22, '2024-11-11 19:00:00', 'Golden 1 Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (1, 12, '2024-11-11 19:30:00', 'State Farm Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (6, 21, '2024-11-11 20:00:00', 'Rocket Mortgage FieldHouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (16, 5, '2024-11-11 20:30:00', 'Kaseya Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (10, 30, '2024-11-11 21:00:00', 'Chase Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (4, 8, '2024-11-11 22:00:00', 'Spectrum Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (7, 9, '2024-11-12 20:00:00', 'American Airlines Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (27, 18, '2024-11-12 20:30:00', 'AT&T Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (20, 11, '2024-11-12 21:00:00', 'Madison Square Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (21, 2, '2024-11-12 21:30:00', 'Paycom Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (19, 3, '2024-11-12 22:00:00', 'Smoothie King Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (10, 24, '2024-11-12 22:30:00', 'Chase Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (23, 28, '2024-11-12 23:00:00', 'Wells Fargo Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (16, 22, '2024-11-13 19:00:00', 'Kaseya Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (17, 15, '2024-11-13 19:30:00', 'Fiserv Forum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (8, 14, '2024-11-13 20:00:00', 'Ball Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (12, 6, '2024-11-13 20:30:00', 'Gainbridge Fieldhouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (26, 25, '2024-11-13 21:00:00', 'Golden 1 Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (13, 4, '2024-11-13 21:30:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (7, 29, '2024-11-13 22:00:00', 'American Airlines Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (30, 5, '2024-11-13 22:30:00', 'Capital One Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (16, 2, '2024-11-14 19:00:00', 'Kaseya Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (10, 11, '2024-11-14 20:00:00', 'Chase Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (15, 24, '2024-11-14 21:00:00', 'FedExForum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (25, 28, '2024-11-14 21:30:00', 'Moda Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (22, 19, '2024-11-15 19:00:00', 'Amway Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (8, 13, '2024-11-15 20:00:00', 'Ball Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (1, 23, '2024-11-15 20:30:00', 'State Farm Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (6, 18, '2024-11-15 20:30:00', 'Rocket Mortgage FieldHouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (28, 21, '2024-11-15 21:00:00', 'Scotiabank Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (14, 7, '2024-11-15 22:00:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (26, 29, '2024-11-15 22:30:00', 'Golden 1 Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (20, 2, '2024-11-16 19:30:00', 'Madison Square Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (12, 11, '2024-11-16 20:00:00', 'Gainbridge Fieldhouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (27, 5, '2024-11-16 21:00:00', 'AT&T Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (25, 17, '2024-11-16 21:30:00', 'Moda Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (4, 15, '2024-11-16 22:00:00', 'Spectrum Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (10, 13, '2024-11-16 22:30:00', 'Chase Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (24, 29, '2024-11-16 23:00:00', 'Footprint Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (3, 16, '2024-11-17 19:00:00', 'Barclays Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (23, 18, '2024-11-17 19:30:00', 'Wells Fargo Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (9, 30, '2024-11-17 20:00:00', 'Little Caesars Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (17, 26, '2024-11-17 21:00:00', 'Fiserv Forum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (21, 7, '2024-11-17 21:30:00', 'Paycom Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (13, 15, '2024-11-17 22:00:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (19, 8, '2024-11-17 22:30:00', 'Smoothie King Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (20, 25, '2024-11-18 19:00:00', 'Madison Square Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (22, 12, '2024-11-18 20:00:00', 'Amway Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (16, 14, '2024-11-18 20:30:00', 'Kaseya Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (27, 30, '2024-11-18 21:00:00', 'AT&T Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (15, 17, '2024-11-18 22:00:00', 'FedExForum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (19, 9, '2024-11-19 19:30:00', 'Smoothie King Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (14, 10, '2024-11-19 20:00:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (24, 23, '2024-11-19 21:00:00', 'Footprint Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (7, 29, '2024-11-19 21:30:00', 'American Airlines Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (8, 18, '2024-11-19 22:00:00', 'Ball Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (3, 21, '2024-11-19 22:30:00', 'Barclays Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (20, 13, '2024-11-20 19:00:00', 'Madison Square Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (5, 30, '2024-11-20 20:00:00', 'United Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (26, 15, '2024-11-20 20:30:00', 'Golden 1 Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (10, 12, '2024-11-20 21:00:00', 'Chase Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (2, 25, '2024-11-20 21:30:00', 'TD Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (18, 27, '2024-11-20 22:00:00', 'Target Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (29, 4, '2024-11-20 22:30:00', 'Vivint Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (11, 22, '2024-11-21 19:00:00', 'Toyota Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (8, 9, '2024-11-21 20:00:00', 'Ball Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (12, 24, '2024-11-21 21:00:00', 'Gainbridge Fieldhouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (23, 20, '2024-11-21 21:30:00', 'Wells Fargo Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (19, 3, '2024-11-21 22:00:00', 'Smoothie King Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (13, 14, '2024-11-21 22:30:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (6, 1, '2024-11-22 19:00:00', 'Rocket Mortgage FieldHouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (21, 15, '2024-11-22 20:00:00', 'Paycom Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (4, 29, '2024-11-22 20:30:00', 'Spectrum Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (2, 10, '2024-11-22 21:00:00', 'TD Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (16, 18, '2024-11-22 21:30:00', 'Kaseya Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (17, 27, '2024-11-22 22:00:00', 'Fiserv Forum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (28, 19, '2024-11-22 22:30:00', 'Scotiabank Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (30, 14, '2024-11-23 19:00:00', 'Capital One Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (26, 9, '2024-11-23 20:00:00', 'Golden 1 Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (20, 4, '2024-11-23 20:30:00', 'Madison Square Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (5, 16, '2024-11-23 21:00:00', 'United Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (24, 25, '2024-11-23 21:30:00', 'Footprint Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (11, 18, '2024-11-23 22:00:00', 'Toyota Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (17, 15, '2024-11-23 22:30:00', 'Fiserv Forum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (7, 3, '2024-11-24 19:00:00', 'American Airlines Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (10, 1, '2024-11-24 19:30:00', 'Chase Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (23, 2, '2024-11-24 20:00:00', 'Wells Fargo Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (22, 8, '2024-11-24 20:30:00', 'Amway Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (6, 20, '2024-11-24 21:00:00', 'Rocket Mortgage FieldHouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (27, 26, '2024-11-24 22:00:00', 'AT&T Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (14, 25, '2024-11-24 22:30:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (7, 10, '2024-11-25 19:00:00', 'American Airlines Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (29, 11, '2024-11-25 20:00:00', 'Vivint Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (1, 18, '2024-11-25 20:30:00', 'State Farm Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (9, 3, '2024-11-25 21:00:00', 'Little Caesars Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (13, 12, '2024-11-25 21:30:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (30, 4, '2024-11-25 22:00:00', 'Capital One Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (22, 26, '2024-11-26 19:00:00', 'Amway Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (18, 17, '2024-11-26 20:00:00', 'Target Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (21, 19, '2024-11-26 20:30:00', 'Paycom Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (5, 9, '2024-11-26 21:00:00', 'United Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (15, 24, '2024-11-26 21:30:00', 'FedExForum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (16, 14, '2024-11-26 22:00:00', 'Kaseya Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (28, 6, '2024-11-26 22:30:00', 'Scotiabank Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (1, 8, '2024-11-27 19:00:00', 'State Farm Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (30, 12, '2024-11-27 19:30:00', 'Capital One Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (13, 16, '2024-11-27 20:00:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (11, 14, '2024-11-27 21:00:00', 'Toyota Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (27, 7, '2024-11-27 21:30:00', 'AT&T Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (17, 15, '2024-11-27 22:00:00', 'Fiserv Forum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (24, 25, '2024-11-27 22:30:00', 'Footprint Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (23, 20, '2024-11-28 19:00:00', 'Wells Fargo Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (16, 29, '2024-11-28 19:30:00', 'Kaseya Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (10, 22, '2024-11-28 20:00:00', 'Chase Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (18, 4, '2024-11-28 20:30:00', 'Target Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (19, 12, '2024-11-28 21:00:00', 'Smoothie King Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (14, 5, '2024-11-28 21:30:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (3, 13, '2024-11-29 19:00:00', 'Barclays Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (6, 15, '2024-11-29 19:30:00', 'Rocket Mortgage FieldHouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (25, 9, '2024-11-29 20:00:00', 'Moda Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (1, 2, '2024-11-29 20:30:00', 'State Farm Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (16, 26, '2024-11-29 21:00:00', 'Kaseya Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (8, 24, '2024-11-29 21:30:00', 'Ball Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (7, 28, '2024-11-29 22:00:00', 'American Airlines Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (23, 11, '2024-11-30 19:30:00', 'Wells Fargo Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (19, 22, '2024-11-30 20:00:00', 'Smoothie King Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (14, 21, '2024-11-30 21:00:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (17, 12, '2024-11-30 21:30:00', 'Fiserv Forum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (13, 20, '2024-11-30 22:00:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (18, 26, '2024-11-30 22:30:00', 'Target Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (10, 6, '2024-11-30 23:00:00', 'Chase Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (30, 4, '2024-12-01 19:00:00', 'Capital One Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (27, 29, '2024-12-01 19:30:00', 'AT&T Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (22, 15, '2024-12-01 20:00:00', 'Amway Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (7, 5, '2024-12-01 20:30:00', 'American Airlines Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (20, 17, '2024-12-01 21:00:00', 'Madison Square Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (24, 8, '2024-12-01 22:30:00', 'Footprint Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (2, 19, '2024-12-02 19:00:00', 'TD Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (16, 28, '2024-12-02 19:30:00', 'Kaseya Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (26, 21, '2024-12-02 20:00:00', 'Golden 1 Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (1, 3, '2024-12-02 20:30:00', 'State Farm Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (15, 9, '2024-12-02 21:00:00', 'FedExForum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (25, 7, '2024-12-02 22:00:00', 'Moda Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (12, 13, '2024-12-03 19:00:00', 'Gainbridge Fieldhouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (27, 8, '2024-12-03 20:00:00', 'AT&T Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (20, 2, '2024-12-03 21:00:00', 'Madison Square Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (22, 18, '2024-12-03 21:30:00', 'Amway Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (30, 14, '2024-12-03 22:00:00', 'Capital One Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (23, 24, '2024-12-03 22:30:00', 'Wells Fargo Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (29, 17, '2024-12-04 19:00:00', 'Vivint Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (6, 4, '2024-12-04 19:30:00', 'Rocket Mortgage FieldHouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (13, 5, '2024-12-04 20:00:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (8, 11, '2024-12-04 21:00:00', 'Ball Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (26, 15, '2024-12-04 21:30:00', 'Golden 1 Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (17, 7, '2024-12-04 22:00:00', 'Fiserv Forum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (21, 16, '2024-12-04 22:30:00', 'Paycom Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (24, 9, '2024-12-05 19:00:00', 'Footprint Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (28, 12, '2024-12-05 20:00:00', 'Scotiabank Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (22, 13, '2024-12-05 20:30:00', 'Amway Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (10, 3, '2024-12-05 21:00:00', 'Chase Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (30, 8, '2024-12-05 21:30:00', 'Capital One Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (11, 23, '2024-12-05 22:00:00', 'Toyota Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (27, 1, '2024-12-06 19:30:00', 'AT&T Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (4, 7, '2024-12-06 20:00:00', 'Spectrum Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (20, 24, '2024-12-06 21:00:00', 'Madison Square Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (17, 15, '2024-12-06 21:30:00', 'Fiserv Forum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (5, 14, '2024-12-06 22:00:00', 'United Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (3, 2, '2024-12-07 19:30:00', 'Barclays Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (6, 19, '2024-12-07 20:00:00', 'Rocket Mortgage FieldHouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (28, 26, '2024-12-07 20:30:00', 'Scotiabank Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (16, 9, '2024-12-07 21:00:00', 'Kaseya Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (21, 8, '2024-12-07 21:30:00', 'Paycom Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (12, 11, '2024-12-07 22:00:00', 'Gainbridge Fieldhouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (22, 10, '2024-12-07 22:30:00', 'Amway Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (1, 27, '2024-12-08 19:00:00', 'State Farm Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (30, 4, '2024-12-08 19:30:00', 'Capital One Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (26, 5, '2024-12-08 20:00:00', 'Golden 1 Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (14, 24, '2024-12-08 20:30:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (19, 2, '2024-12-08 21:00:00', 'Smoothie King Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (15, 11, '2024-12-08 21:30:00', 'FedExForum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (9, 12, '2024-12-08 22:00:00', 'Little Caesars Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (27, 25, '2024-12-09 19:00:00', 'AT&T Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (20, 8, '2024-12-09 19:30:00', 'Madison Square Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (18, 17, '2024-12-09 20:00:00', 'Target Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (3, 29, '2024-12-09 20:30:00', 'Barclays Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (5, 14, '2024-12-09 21:00:00', 'United Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (15, 4, '2024-12-09 21:30:00', 'FedExForum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (10, 21, '2024-12-09 22:00:00', 'Chase Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (23, 18, '2024-12-10 19:00:00', 'Wells Fargo Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (11, 1, '2024-12-10 20:00:00', 'Toyota Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (22, 9, '2024-12-10 20:30:00', 'Amway Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (12, 13, '2024-12-10 21:00:00', 'Gainbridge Fieldhouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (16, 26, '2024-12-10 21:30:00', 'Kaseya Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (6, 28, '2024-12-10 22:00:00', 'Rocket Mortgage FieldHouse');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (19, 4, '2024-12-11 19:30:00', 'Smoothie King Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (14, 24, '2024-12-11 20:00:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (2, 23, '2024-12-11 21:00:00', 'TD Garden');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (17, 7, '2024-12-11 21:30:00', 'Fiserv Forum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (28, 21, '2024-12-11 22:00:00', 'Scotiabank Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (29, 5, '2024-12-11 22:30:00', 'Vivint Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (9, 11, '2024-12-11 23:00:00', 'Little Caesars Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (22, 10, '2024-12-12 19:00:00', 'Amway Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (23, 12, '2024-12-12 19:30:00', 'Wells Fargo Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (7, 6, '2024-12-12 20:00:00', 'American Airlines Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (3, 17, '2024-12-12 21:00:00', 'Barclays Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (1, 19, '2024-12-12 21:30:00', 'State Farm Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (4, 16, '2024-12-12 22:00:00', 'Spectrum Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (8, 26, '2024-12-12 22:30:00', 'Ball Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (15, 14, '2024-12-12 23:00:00', 'FedExForum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (13, 24, '2024-12-13 19:30:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (17, 9, '2024-12-13 20:00:00', 'Fiserv Forum');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (11, 30, '2024-12-13 21:00:00', 'Toyota Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (26, 2, '2024-12-13 21:30:00', 'Golden 1 Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (14, 23, '2024-12-14 19:00:00', 'Crypto.com Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (28, 5, '2024-12-14 19:30:00', 'Scotiabank Arena');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (3, 21, '2024-12-14 20:00:00', 'Barclays Center');
INSERT INTO schedule (home_team_id, away_team_id, game_date, location)
VALUES (4, 25, '2024-12-14 20:30:00', 'Spectrum Center');