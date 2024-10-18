-- Add year int column to game table (using game_year instead of year to avoid conflict with SQL keyword)
ALTER TABLE game ADD COLUMN game_year INT NOT NULL DEFAULT 0;
CREATE INDEX idx_game_year ON game (game_year);