package com.peated.valhack.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peated.valhack.model.LeagueData;
import com.peated.valhack.model.Tournament;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@Service
public class LeagueDataRepository {
    private final Map<Tournament, List<LeagueData>> leagueDataCache = new HashMap<>();

    private List<LeagueData> loadLeagueData(Tournament tournament) {
        if (leagueDataCache.containsKey(tournament)) {
            return leagueDataCache.get(tournament);
        }

        try (InputStream inputStream = new FileInputStream("./data/" + tournament.getFolderName() + "/leagues.json.gz");
             GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
             InputStreamReader reader = new InputStreamReader(gzipInputStream);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            ObjectMapper objectMapper = new ObjectMapper();
            var leagueData = objectMapper.readValue(bufferedReader, new TypeReference<List<LeagueData>>() {
            });
            leagueDataCache.put(tournament, leagueData);
            return leagueData;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load team data", e);
        }
    }

    public LeagueData getLeagueDataByMappingDataId(Tournament tournament, String leagueId) {
        return this.loadLeagueData(tournament).stream()
                .filter(leagueData -> leagueData.leagueId().equals(leagueId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("League data not found for league id: " + leagueId));
    }
}
