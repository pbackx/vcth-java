package com.peated.valhack.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peated.valhack.model.MappingData;


@Component
public class MappingDataRepository {
    private final List<MappingData> mappingDataList;

    public MappingDataRepository() {
        this.mappingDataList = loadMappingData();
    }

    private List<MappingData> loadMappingData() {
        try (InputStream inputStream = getClass().getResourceAsStream("/data/mapping_data.json.gz");
             GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
             InputStreamReader reader = new InputStreamReader(gzipInputStream);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(bufferedReader, new TypeReference<List<MappingData>>() {});

        } catch (IOException e) {
            throw new RuntimeException("Failed to load mapping data", e);
        }
    }

    public List<MappingData> getAllMappingData() {
        return mappingDataList;
    }

    public MappingData getMappingDataByPlatformGameId(String platformGameId) {
        return mappingDataList.stream()
            .filter(mappingData -> mappingData.platformGameId().equals(platformGameId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Mapping data not found for platform game id: " + platformGameId));
    }

}
