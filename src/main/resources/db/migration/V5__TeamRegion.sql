ALTER TABLE team ADD COLUMN region VARCHAR(255) NOT NULL DEFAULT '';
CREATE INDEX idx_team_region ON team (region);