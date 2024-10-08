CREATE TABLE IF NOT EXISTS tournament
(
    id   INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

INSERT INTO tournament (id, name)
VALUES (1, 'GAME_CHANGERS'),
       (2, 'VCT_CHALLENGERS'),
       (3, 'VCT_INTERNATIONAL');

ALTER TABLE game ADD COLUMN tournament_id INT;
ALTER TABLE game ADD FOREIGN KEY (tournament_id) REFERENCES tournament(id);

UPDATE game
SET tournament_id = 3
WHERE tournament_id IS NULL;

ALTER TABLE game ALTER COLUMN tournament_id SET NOT NULL;
