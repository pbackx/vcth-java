package com.peated.valhack.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peated.valhack.model.PlayerData;
import com.peated.valhack.model.Tournament;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@Component
public class PlayerDataRepository {
    private final Map<String, PlayerData> playerDataCache = new HashMap<>();

    private void loadPlayerData() {
        for (Tournament tournament : Tournament.values()) {
            try (InputStream inputStream = new FileInputStream("./data/" + tournament.getFolderName() + "/players.json.gz");
                 GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
                 InputStreamReader reader = new InputStreamReader(gzipInputStream);
                 BufferedReader bufferedReader = new BufferedReader(reader)) {

                ObjectMapper objectMapper = new ObjectMapper();
                var playerData = objectMapper.readValue(bufferedReader, new TypeReference<List<PlayerData>>() {});
                playerData.forEach(data -> playerDataCache.put(data.id(), data));
            } catch (IOException e) {
                throw new RuntimeException("Failed to load player data", e);
            }
        }
    }

    public PlayerData getPlayerDataByMappingDataId(String playerId) {
        if (playerDataCache.isEmpty()) {
            loadPlayerData();
        }

        return playerDataCache.get(playerId);
    }
}
