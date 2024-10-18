package com.peated.valhack.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peated.valhack.model.TeamData;
import com.peated.valhack.model.Tournament;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@Service
public class TeamDataRepository {
    private final Map<Tournament, List<TeamData>> teamDataCache = new HashMap<>();

    private List<TeamData> loadTeamData(Tournament tournament) {
        if (teamDataCache.containsKey(tournament)) {
            return teamDataCache.get(tournament);
        }

        try (InputStream inputStream = new FileInputStream("./data/" + tournament.getFolderName() + "/teams.json.gz");
             GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
             InputStreamReader reader = new InputStreamReader(gzipInputStream);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            ObjectMapper objectMapper = new ObjectMapper();
            var teamData = objectMapper.readValue(bufferedReader, new TypeReference<List<TeamData>>() {});
            teamDataCache.put(tournament, teamData);
            return teamData;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load team data", e);
        }
    }

    public TeamData getTeamDataByMappingDataId(Tournament tournament, String teamId) {
        return this.loadTeamData(tournament).stream()
            .filter(teamData -> teamData.id().equals(teamId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Team data not found for team id: " + teamId));
    }
}
