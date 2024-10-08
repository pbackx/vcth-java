CREATE INDEX idx_agent_guid ON agent (guid);
CREATE INDEX idx_game_platform_game_id ON game (platform_game_id);
CREATE INDEX idx_player_mapping_data_id ON player (mapping_data_id);
CREATE INDEX idx_selected_agent_game_id ON selected_agent (game_id);
CREATE INDEX idx_selected_agent_game_id_player_id ON selected_agent (game_id, player_id);
CREATE INDEX idx_team_mapping_data_id ON team (mapping_data_id);