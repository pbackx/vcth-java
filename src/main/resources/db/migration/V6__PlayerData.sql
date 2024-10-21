-- add handle, first_name and last_name columns to the player table, they can be nullable
ALTER TABLE player ADD COLUMN handle VARCHAR(255);
ALTER TABLE player ADD COLUMN first_name VARCHAR(255);
ALTER TABLE player ADD COLUMN last_name VARCHAR(255);
