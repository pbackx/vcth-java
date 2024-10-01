package com.peated.valhack.val;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peated.valhack.model.Player;
import com.peated.valhack.model.Team;
import com.peated.valhack.repository.PlayerRepository;
@Component
public class ValParser {
    @Autowired
    private PlayerRepository playerRepository;

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
                players.put(localPlayerId, this.createOrUpdatePlayer(player));
            }

            result.append("\nPlayers:\n");
            for (Map.Entry<Integer, Player> entry : players.entrySet()) {
                result.append(entry.getValue())
                      .append("\n");
            }

        }

        return result.toString();
    }

    private String getExternalId(JsonNode player) {
        try {
            var accountId = player.get("accountId");
            return accountId.get("type").asText() + ":" + accountId.get("value").asText();
        } catch (Exception e) {
            System.out.println("Error getting external ID for player: " + player);
            return null;
        }
    }

    private Player createOrUpdatePlayer(JsonNode player) {
        var playerObj = new Player(null, player.get("displayName").asText(), getExternalId(player));

        if (playerRepository.existsByExternalId(playerObj.externalId())) {
            Player existingPlayer = playerRepository.findByExternalId(playerObj.externalId()).get();
            existingPlayer = new Player(existingPlayer.id(), playerObj.name(), existingPlayer.externalId());
            return playerRepository.save(existingPlayer);
        } else {
        return playerRepository.save(playerObj);
        }
    }

}
