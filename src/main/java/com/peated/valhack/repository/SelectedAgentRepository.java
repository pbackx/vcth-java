package com.peated.valhack.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.peated.valhack.model.SelectedAgent;

@Repository
public class SelectedAgentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveOrUpdate(SelectedAgent selectedAgent) {
        String selectQuery = "SELECT COUNT(*) FROM selected_agent WHERE game_id = ? AND player_id = ?";
        int count = jdbcTemplate.queryForObject(selectQuery, Integer.class, selectedAgent.gameId(), selectedAgent.playerId());

        if (count > 0) {
            String updateQuery = "UPDATE selected_agent SET agent_id = ? WHERE game_id = ? AND player_id = ?";
            jdbcTemplate.update(updateQuery, selectedAgent.agentId(), selectedAgent.gameId(), selectedAgent.playerId());
        } else {
            String insertQuery = "INSERT INTO selected_agent (game_id, player_id, agent_id) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertQuery, selectedAgent.gameId(), selectedAgent.playerId(), selectedAgent.agentId());
        }
    }

    public List<SelectedAgent> findByGameId(int gameId) {
        return jdbcTemplate.query("SELECT * FROM selected_agent WHERE game_id = ?", (rs, rowNum) -> new SelectedAgent(
            rs.getInt("game_id"),
            rs.getInt("player_id"),
            rs.getInt("agent_id")
        ), gameId);
    }
}
