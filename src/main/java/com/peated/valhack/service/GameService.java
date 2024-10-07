package com.peated.valhack.service;

import com.peated.valhack.model.DataFile;
import com.peated.valhack.model.DataFileStatus;
import com.peated.valhack.model.Tournament;
import com.peated.valhack.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final GameDataProvider gameDataProvider;

    public GameService(GameRepository gameRepository, GameDataProvider gameDataProvider) {
        this.gameRepository = gameRepository;
        this.gameDataProvider = gameDataProvider;
    }

    /**
     * @param gameFileName the name of the game file, for instance "val_00676ae0-0a4d-4577-be1e-ab4d3a9889aa.json.gz"
     * @return whether the file was already processed
     */
    public DataFileStatus getStatus(String gameFileName) {
        try {
            String gameId = gameFileName.substring(0, gameFileName.indexOf(".")).replace("val_", "val:");
            if (gameRepository.existsByPlatformGameId(gameId)) {
                return DataFileStatus.DONE;
            } else {
                return DataFileStatus.TO_PROCESS;
            }
        } catch (Exception e) {
            return DataFileStatus.ERROR;
        }
    }

    public List<DataFile> getGameFiles(Tournament tournament, String year) {
        var dataFiles = gameDataProvider.getDataFiles(tournament, year);
        return dataFiles.stream()
                .map(dataFile -> new DataFile(dataFile, this.getStatus(dataFile), tournament, year))
                .toList();
    }
}
