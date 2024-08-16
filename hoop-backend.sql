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
    reminder_time TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (team_id) REFERENCES teams(id)
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