package com.peated.valhack.service;

import com.peated.valhack.model.DataFile;
import com.peated.valhack.model.DataFileStatus;
import com.peated.valhack.val.ValParser;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class BackgroundGameDataProcessingService {

    private final GameDataProvider gameDataProvider;
    private final ValParser valParser;
    private final GameService gameService;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private String status = "Stopped";

    public BackgroundGameDataProcessingService(GameDataProvider gameDataProvider, ValParser valParser, GameService gameService) {
        this.gameDataProvider = gameDataProvider;
        this.valParser = valParser;
        this.gameService = gameService;
    }

    @Async
    public void startProcess() {
        running.set(true);
        status = "Loading list of unprocessed files.";

        var unprocessedDataFiles = gameService.getGameFiles().stream()
                .filter(dataFile -> dataFile.status().equals(DataFileStatus.TO_PROCESS))
                .map(DataFile::fileName)
                .toList();

        status = unprocessedDataFiles.size() + " files to process.";

        var count = unprocessedDataFiles.size();
        while (running.get()) {
            var fileName = unprocessedDataFiles.get(count - 1);
            status = "Processing " + fileName + ". " + count + " files left.";
            var resource = gameDataProvider.getDataFile(fileName);
            try {
                valParser.parse(resource);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                count--;
                if (count == 0) {
                    running.set(false);
                    status = "Finished processing all files.";
                }
            }
        }
        status = "Stopped";
    }

    public void stopProcess() {
        running.set(false);
        status = "Stopping";
    }

    public boolean isRunning() {
        return running.get();
    }

    public String getStatus() {
        return status;
    }
}