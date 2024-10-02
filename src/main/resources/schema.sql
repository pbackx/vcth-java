CREATE TABLE IF NOT EXISTS team (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    mapping_data_id VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS player (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    mapping_data_id VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS player_team (
    player_id INT,
    team_id INT,
    PRIMARY KEY (player_id, team_id),
    FOREIGN KEY (player_id) REFERENCES player(id),
    FOREIGN KEY (team_id) REFERENCES team(id)
);

CREATE TABLE IF NOT EXISTS game (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    platform_game_id VARCHAR(255) UNIQUE NOT NULL,
    winning_team_id INT,
    losing_team_id INT,
    FOREIGN KEY (winning_team_id) REFERENCES team(id),
    FOREIGN KEY (losing_team_id) REFERENCES team(id)
);

CREATE TABLE IF NOT EXISTS roles (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Delete all existing entries from the roles table
DELETE FROM roles;

-- Insert the desired roles
INSERT INTO roles (id, name) VALUES
(1, 'Duelist'),
(2, 'Initiator'),
(3, 'Controller'),
(4, 'Sentinel');

CREATE TABLE IF NOT EXISTS agent (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    guid VARCHAR(255) UNIQUE NOT NULL,
    role_id INT,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS selected_agent (
    game_id INT,
    player_id INT,
    agent_id INT,
    PRIMARY KEY (game_id, player_id, agent_id),
    FOREIGN KEY (game_id) REFERENCES game(id),
    FOREIGN KEY (player_id) REFERENCES player(id),
    FOREIGN KEY (agent_id) REFERENCES agent(id)
);

-- Delete all existing entries from tables except roles
DELETE FROM selected_agent;
DELETE FROM agent;
DELETE FROM game;
DELETE FROM player_team;
DELETE FROM player;
DELETE FROM team;
