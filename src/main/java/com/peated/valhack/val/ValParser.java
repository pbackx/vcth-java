package com.peated.valhack.val;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peated.valhack.model.Player;
import com.peated.valhack.model.PlayerRef;
import com.peated.valhack.model.Team;
import com.peated.valhack.repository.MappingDataRepository;
import com.peated.valhack.repository.PlayerRepository;
import com.peated.valhack.repository.TeamRepository;

@Component
public class ValParser {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MappingDataRepository mappingDataRepository;

    public ValParser() {
    }

    public String parse(Resource resource) throws IOException {
        var result = new StringBuilder();
        try (FileInputStream fileInputStream = new FileInputStream(resource.getFile());
             var gzipInputStream = new GZIPInputStream(fileInputStream);
             var reader = new InputStreamReader(gzipInputStream);
             var bufferedReader = new BufferedReader(reader)) {
            
            var jsonContent = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonContent.append(line);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonContent.toString());

            result.append("Total number of entries: " + jsonNode.size() + "\n");

            var platformGameId = jsonNode.get(0).get("platformGameId").asText();

            JsonNode configurationNode = null;
            JsonNode gameDecidedNode = null;
            for (JsonNode entry : jsonNode) {
                if (entry.has("configuration") && configurationNode == null) {
                    configurationNode = entry.get("configuration");
                } else if (entry.has("gameDecided") && gameDecidedNode == null) {
                    gameDecidedNode = entry.get("gameDecided");
                }
                if (configurationNode != null && gameDecidedNode != null) {
                    break;
                }
            }
            
            if (configurationNode == null || gameDecidedNode == null) {
                result.append("Invalid data file. Missing configuration or gameDecided.\n");
                return result.toString();
            }

            var players = new HashMap<Integer, Player>();
            var teams = new HashMap<Integer, Team>();

            for (JsonNode player : configurationNode.get("players")) {
                var localPlayerId = player.get("playerId").get("value").asInt();
                var playerMappingDataId = getPlayerMappingDataId(platformGameId, localPlayerId);
                players.put(localPlayerId, this.createOrUpdatePlayer(player, playerMappingDataId));
            }

            result.append("\nPlayers:\n");
            for (Map.Entry<Integer, Player> entry : players.entrySet()) {
                result.append(entry.getValue())
                      .append("\n");
            }

            for (JsonNode team : configurationNode.get("teams")) {
                var localTeamId = team.get("teamId").get("value").asInt();
                var teamMappingDataId = getTeamMappingDataId(platformGameId, localTeamId);
                teams.put(localTeamId, this.createOrUpdateTeam(team, teamMappingDataId, players));
            }

            result.append("\nTeams:\n");
            for (Map.Entry<Integer, Team> entry : teams.entrySet()) {
                result.append(entry.getValue())
                      .append("\n");
            }

        }

        return result.toString();
    }

    private String getPlayerMappingDataId(String platformGameId, int localPlayerId) {
        var mappingData = mappingDataRepository.getMappingDataByPlatformGameId(platformGameId);
        return mappingData.participantMapping().get(localPlayerId);
    }

    private String getTeamMappingDataId(String platformGameId, int localTeamId) {
        var mappingData = mappingDataRepository.getMappingDataByPlatformGameId(platformGameId);
        return mappingData.teamMapping().get(localTeamId);
    }

    private Player createOrUpdatePlayer(JsonNode player, String playerMappingDataId) {
        var playerObj = new Player(null, player.get("displayName").asText(), playerMappingDataId);

        if (playerRepository.existsByMappingDataId(playerObj.mappingDataId())) {
            Player existingPlayer = playerRepository.findByMappingDataId(playerObj.mappingDataId()).get();
            existingPlayer = new Player(existingPlayer.id(), playerObj.name(), existingPlayer.mappingDataId());
            return playerRepository.save(existingPlayer);
        } else {
        return playerRepository.save(playerObj);
        }
    }

    private Team createOrUpdateTeam(JsonNode team, String teamMappingDataId, Map<Integer, Player> players) {
        var playersInTeam = new HashSet<PlayerRef>();
        for (JsonNode player: team.get("playersInTeam")) {
            var localPlayerId = player.get("value").asInt();
            playersInTeam.add(new PlayerRef(players.get(localPlayerId).id()));
        }

        var teamObj = new Team(
            null, 
            team.get("name").asText(), 
            teamMappingDataId,
            playersInTeam
        );

        return teamRepository.save(teamObj);
    }


}
