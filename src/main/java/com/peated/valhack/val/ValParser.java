package com.peated.valhack.val;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import com.peated.valhack.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peated.valhack.repository.AgentRepository;
import com.peated.valhack.repository.GameRepository;
import com.peated.valhack.repository.MappingDataRepository;
import com.peated.valhack.repository.PlayerRepository;
import com.peated.valhack.repository.SelectedAgentRepository;
import com.peated.valhack.repository.TeamRepository;

@Component
public class ValParser {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private SelectedAgentRepository selectedAgentRepository;

    @Autowired
    private MappingDataRepository mappingDataRepository;

    @Autowired
    private AgentToRoleMapper agentToRoleMapper;

    public ValParser() {
    }

    public String parse(Tournament tournament, Resource resource) throws IOException {
        var result = new StringBuilder();
        try (var gzipInputStream = new GZIPInputStream(resource.getInputStream());
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
            var agentsForPlayer = new HashMap<Integer, Agent>();

            for (JsonNode player : configurationNode.get("players")) {
                var localPlayerId = player.get("playerId").get("value").asInt();
                var playerMappingDataId = getPlayerMappingDataId(tournament, platformGameId, localPlayerId);
                players.put(localPlayerId, this.createOrUpdatePlayer(player, playerMappingDataId));

                var selectedAgent = player.get("selectedAgent");
                if (selectedAgent.get("type").asText().equals("UNKNOWN")) {
                    var fallback = selectedAgent.get("fallback");
                    agentsForPlayer.put(localPlayerId, createOrUpdateAgent(fallback));
                } else {
                    System.out.println("Unknown selected agent type: " + selectedAgent);
                    result.append("Unknown selected agent type: " + selectedAgent + "\n");
                }
            }

            result.append("\nPlayers:\n");
            for (Map.Entry<Integer, Player> entry : players.entrySet()) {
                result.append(entry.getValue())
                      .append("\n");
            }

            for (JsonNode team : configurationNode.get("teams")) {
                var localTeamId = team.get("teamId").get("value").asInt();
                var teamMappingDataId = getTeamMappingDataId(tournament, platformGameId, localTeamId);
                teams.put(localTeamId, this.createOrUpdateTeam(team, teamMappingDataId, players));
            }

            result.append("\nAgents:\n");
            for (Map.Entry<Integer, Agent> entry : agentsForPlayer.entrySet()) {
                result.append(entry.getValue())
                      .append("\n");
            }

            result.append("\nTeams:\n");
            for (Map.Entry<Integer, Team> entry : teams.entrySet()) {
                result.append(entry.getValue())
                      .append("\n");
            }

            if (!gameDecidedNode.get("state").asText().equals("WINNER_DECIDED")) {
                System.out.println("Unexpected gameDecided state: " + gameDecidedNode);
                result.append("Unexpected gameDecided state. Check server logs for details.\n");
                result.append("GameDecided JSON:\n");
                result.append(gameDecidedNode.toPrettyString());
                return result.toString();
            }

            var localWinningTeamId = gameDecidedNode.get("winningTeam").get("value").asInt();
            var localLosingTeamId = teams.keySet().stream()
                .filter(id -> id != localWinningTeamId)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not determine losing team"));

            var winningTeamId = teams.get(localWinningTeamId).id();
            var losingTeamId = teams.get(localLosingTeamId).id();

            var game = createOrUpdateGame(tournament, platformGameId, winningTeamId, losingTeamId);

            for (Map.Entry<Integer, Agent> entry : agentsForPlayer.entrySet()) {
                var localPlayerId = entry.getKey();
                var playerId = players.get(localPlayerId).id();
                var agent = entry.getValue();
                var selectedAgent = new SelectedAgent(
                    game.id(), playerId, agent.id()
                );
                selectedAgentRepository.saveOrUpdate(selectedAgent);
            }

            result.append("\nSelected Agents:\n");
            for (SelectedAgent selectedAgent : selectedAgentRepository.findByGameId(game.id())) {
                result.append(selectedAgent)
                      .append("\n");
            }
        }

        return result.toString();
    }

    private String getPlayerMappingDataId(Tournament tournament, String platformGameId, int localPlayerId) {
        var mappingData = mappingDataRepository.getMappingDataByPlatformGameId(tournament, platformGameId);
        return mappingData.participantMapping().get(localPlayerId);
    }

    private String getTeamMappingDataId(Tournament tournament, String platformGameId, int localTeamId) {
        var mappingData = mappingDataRepository.getMappingDataByPlatformGameId(tournament, platformGameId);
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

        if (teamRepository.existsByMappingDataId(teamObj.mappingDataId())) {
            Team existingTeam = teamRepository.findByMappingDataId(teamObj.mappingDataId()).get();
            existingTeam = new Team(existingTeam.id(), teamObj.name(), existingTeam.mappingDataId(), teamObj.players());
            return teamRepository.save(existingTeam);
        } else {
            return teamRepository.save(teamObj);
        }
    }

    private Game createOrUpdateGame(Tournament tournament, String platformGameId, int winningTeamId, int losingTeamId) {
        var game = new Game(null, platformGameId, winningTeamId, losingTeamId, tournament.getId());
        if (gameRepository.existsByPlatformGameId(platformGameId)) {
            Game existingGame = gameRepository.findByPlatformGameId(platformGameId).get();
            return gameRepository.save(new Game(existingGame.id(), platformGameId, winningTeamId, losingTeamId, tournament.getId()));
        } else {
            return gameRepository.save(game);
        }
    }

    private Agent createOrUpdateAgent(JsonNode fallback) {
        var agentGuid = fallback.get("guid").asText();
        var agent = new Agent(
            null, 
            agentGuid, 
            agentToRoleMapper.getRole(agentGuid)
        );

        if (agentRepository.existsByGuid(agent.guid())) {
            return agentRepository.findByGuid(agent.guid()).get();
        } else {
            return agentRepository.save(agent);
        }
    }
}
