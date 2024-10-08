package com.peated.valhack.repository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import com.peated.valhack.model.Tournament;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peated.valhack.model.MappingData;


@Component
public class MappingDataRepository {
    private final Map<Tournament, List<MappingData>> mappingDataCache = new HashMap<>();

    public MappingDataRepository() {
    }

    private List<MappingData> loadMappingData(Tournament tournament) {
        if (mappingDataCache.containsKey(tournament)) {
            return mappingDataCache.get(tournament);
        }

        try (InputStream inputStream = new FileInputStream("./data/" + tournament.getFolderName() + "/mapping_data_v2.json.gz");
             GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
             InputStreamReader reader = new InputStreamReader(gzipInputStream);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            ObjectMapper objectMapper = new ObjectMapper();
            var mappingData = objectMapper.readValue(bufferedReader, new TypeReference<List<MappingData>>() {});
            mappingDataCache.put(tournament, mappingData);
            return mappingData;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load mapping data", e);
        }
    }

    public MappingData getMappingDataByPlatformGameId(Tournament tournament, String platformGameId) {
        return this.loadMappingData(tournament).stream()
            .filter(mappingData -> mappingData.platformGameId().equals(platformGameId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Mapping data not found for platform game id: " + platformGameId));
    }

}
